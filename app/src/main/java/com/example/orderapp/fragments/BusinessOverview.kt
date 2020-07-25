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
import com.example.orderapp.databinding.FragmentBusinessOverviewBinding
import com.example.orderapp.model.Business
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
        binding.business = Business()
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
            //TODO get info from repo instead
            businessID = code
            business?.name = "Business with id ${code}"
            business?.type = "Bruh"
            invalidateAll()
            bttnOrder.visibility = View.VISIBLE
        }
        Toast.makeText(activity, "Scanned: " + code, Toast.LENGTH_LONG).show()
    }

    private fun codeCancelled(){
        Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show()
    }
}