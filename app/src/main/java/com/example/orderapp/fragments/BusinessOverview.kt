package com.example.orderapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.orderapp.R
import com.example.orderapp.databinding.FragmentBusinessOverviewBinding
import com.example.orderapp.model.Business
import com.google.zxing.integration.android.IntentIntegrator

class BusinessOverview : Fragment() {

    lateinit var binding: FragmentBusinessOverviewBinding

    private var businessID : String = ""

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
        binding.btnScanCode.setOnClickListener { clickBttnScanCode() }
        binding.bttnOrder.setOnClickListener { clickBttnOrder() }
        binding.bttnOrder.visibility = View.GONE

        return binding.root
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
        view?.findNavController()?.navigate(BusinessOverviewDirections.actionBusinessOverviewToMenu(businessID, binding.business!!.name))
    }

    private fun clickBttnScanCode(){
        //setup scanner
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setBeepEnabled(false)
        //start scan
        integrator.initiateScan()
    }

    private fun codeScanned(code : String){
        binding.apply {
            //get info from model
            business?.name = code
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