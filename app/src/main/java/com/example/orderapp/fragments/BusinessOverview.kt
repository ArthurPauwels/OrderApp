package com.example.orderapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.Menu
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.orderapp.R
import com.example.orderapp.data.repositories.BusinessRepository
import com.example.orderapp.databinding.FragmentBusinessOverviewBinding
import com.example.orderapp.model.BusinessDTO
import com.google.zxing.integration.android.IntentIntegrator

class BusinessOverview : Fragment() {

    lateinit var binding: FragmentBusinessOverviewBinding

    private var businessID : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_business_overview, container, false)

        //setup data
        binding.business = BusinessDTO()
        //setup buttons
        binding.bttnOrder.setOnClickListener { clickBttnOrder() }
        binding.bttnOrder.visibility = View.GONE

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.business_overview_menu, menu)
        //disable share option if needed
        if (getShareIntent().resolveActivity(requireActivity().packageManager) == null) disableShareOption(menu)
        if (businessID == null) disableShareOption(menu)
    }

    private fun disableShareOption(menu: Menu){
        menu.findItem(R.id.shareBusiness).setVisible(false)
    }

    private fun getShareIntent() : Intent {
        //TODO add better sharing text things
        return ShareCompat.IntentBuilder.from(requireActivity()).setText("placeholder Sharing Text that should be replaced soon").setType("text/plain").intent
    }

    private fun shareBusiness(){
        startActivity(getShareIntent())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareBusiness -> shareBusiness()
            R.id.scanCode -> scanCode()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    codeCancelled()
                } else {
                    codeScanned(result.contents)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun clickBttnOrder(){
        //view?.findNavController()?.navigate(BusinessOverviewDirections.actionBusinessOverviewToBusinessNotOpen())
        //todo make this less of a mess
        if(businessID == null) {
            showLongToast("Error: no businessID")
            return
        }
        if (binding.business == null){
            showLongToast("Error: no business")
            return
        }
        view?.findNavController()?.navigate(BusinessOverviewDirections.actionBusinessOverviewToMenu(businessID!!, binding.business!!.name))
    }

    private fun scanCode(){
        //setup scanner
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setBeepEnabled(false)
        //start scan
        integrator.initiateScan()
    }

    private fun codeScanned(code : String){
        binding.apply {
            //TODO parse code into business and table
            businessID = code
            binding.business = BusinessRepository.getBusinessByID(code).toDTO()
            invalidateAll()
            bttnOrder.visibility = View.VISIBLE
        }
        showLongToast("Scanned: " + code)
    }

    private fun codeCancelled(){
        showLongToast("Cancelled")
    }

    private fun showLongToast(message:String){
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}