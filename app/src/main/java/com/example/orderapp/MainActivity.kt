package com.example.orderapp

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.orderapp.databinding.ActivityMainBinding
import com.example.orderapp.model.Business
import com.google.zxing.integration.android.IntentIntegrator


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val business: Business = Business()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.business = business
        binding.btnScanCode.setOnClickListener {
            clickBttnScanCode()
        }
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

    private fun clickBttnScanCode(){
        //setup scanner
        val scanner = IntentIntegrator(this)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        scanner.setBeepEnabled(false)
        //start scan
        scanner.initiateScan()
    }

    private fun codeScanned(code : String){
        binding.apply {
            //get info from model
            business?.name = code
            business?.type = "Bruh"
            invalidateAll()
        }
        Toast.makeText(this, "Scanned: " + code, Toast.LENGTH_LONG).show()
    }

    private fun codeCancelled(){
        Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
    }
}