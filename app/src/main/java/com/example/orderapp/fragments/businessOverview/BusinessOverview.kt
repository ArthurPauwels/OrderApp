package com.example.orderapp.fragments.businessOverview

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.orderapp.R
import com.example.orderapp.databinding.FragmentBusinessOverviewBinding
import com.example.orderapp.model.Business
import com.example.orderapp.model.BusinessDTO
import com.google.zxing.integration.android.IntentIntegrator
import timber.log.Timber


class BusinessOverview : Fragment() {

    private lateinit var viewModel : BusinessOverviewViewModel
    lateinit var binding: FragmentBusinessOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        //setup viewModel
        viewModel = ViewModelProvider(this).get(BusinessOverviewViewModel::class.java)
        viewModel.business.observe(viewLifecycleOwner, Observer {it -> updateBusinessInfo(it)})
        viewModel.readyToOrder.observe(viewLifecycleOwner, Observer { it ->  updateState(it)})

        //setup binding
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_business_overview, container, false)
        binding.business = BusinessDTO()

        //setup buttons
        binding.bttnOrder.setOnClickListener { clickBttnOrder() }
        binding.bttnOrder.visibility = View.GONE

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.business_overview_menu, menu)
        //disable share option if needed
        if (getShareIntent().resolveActivity(requireActivity().packageManager) == null) disableShareOption(menu)
        if (!viewModel.hasBusinessSelected) disableShareOption(menu)
    }

    private fun disableShareOption(menu: Menu){
        menu.findItem(R.id.shareBusiness).isVisible = false
    }

    private fun getShareIntent() : Intent {
        //TODO add better sharing text things
        return ShareCompat.IntentBuilder.from(requireActivity()).setText("placeholder Sharing Text that should be replaced soon").setType("text/plain").intent
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareBusiness -> shareBusiness()
            R.id.scanCode -> scanCode()
            R.id.enterCode -> enterCode()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareBusiness(){
        startActivity(getShareIntent())
    }

    private fun scanCode(){
        //setup scanner
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setBeepEnabled(false)
        //start scan
        integrator.initiateScan()
    }

    private fun enterCode(){
        val li = LayoutInflater.from(context)
        val promptsView: View = li.inflate(R.layout.part_code_input, null)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        // set part_code_input.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView)

        val userInput = promptsView.findViewById(R.id.codeInput) as EditText

        // set dialog message
        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton("OK", { dialog, id -> viewModel.handleNewCode(userInput.text.toString())})
            .setNegativeButton("Cancel", { dialog, id -> dialog.cancel()})

        // create alert dialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()

        // show it
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    showLongToast("Cancelled")
                } else {
                    codeScanned(result.contents)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun clickBttnOrder(){
        //todo make this less of a mess
        if(!viewModel.hasBusinessSelected) {
            Timber.e("No businessID when trying to order")
            return
        }
        if (binding.business == null){
            Timber.e("No business in binding when trying to order")
            return
        }
        if(viewModel.business.value!!.isOpen()){
        view?.findNavController()?.navigate(
            BusinessOverviewDirections.actionBusinessOverviewToMenu(
                viewModel.business.value!!.businessID,
                binding.business!!.name
            )
        )}else view?.findNavController()?.navigate(BusinessOverviewDirections.actionBusinessOverviewToBusinessNotOpen())
    }

    private fun codeScanned(code : String){
        binding.apply {
            //TODO parse code into business and table
            Timber.i("Scanned code: %s", code)
            viewModel.handleNewCode(code)
        }
    }

    private fun showLongToast(message:String){
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    fun updateBusinessInfo(business: Business){
            binding.business = business.toDTO()
            binding.invalidateAll()
    }

    fun updateState(ready : Boolean){
        when (ready) {
            true -> binding.bttnOrder.visibility = View.VISIBLE
            false -> binding.bttnOrder.visibility = View.GONE
        }
    }
}