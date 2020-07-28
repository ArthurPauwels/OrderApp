package com.example.orderapp.fragments.businessOverview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.orderapp.data.repositories.BusinessRepository
import com.example.orderapp.model.Business
import com.google.zxing.integration.android.IntentResult
import timber.log.Timber

class BusinessOverviewViewModel : ViewModel() {

    private val _business = MutableLiveData<Business>()
    val business: LiveData<Business> get() = _business

    private val _navigationEvent = MutableLiveData<OverviewNavigationEvent>()
    val navigationEvent: LiveData<OverviewNavigationEvent> get() = _navigationEvent

    private val _table = MutableLiveData<Int>()

    private val _overviewState = MutableLiveData<OverviewState>()
    val overviewState : LiveData<OverviewState> get() = _overviewState

    val ratingString = Transformations.map(business) { b ->
        if (business.value?.rating == null) {
            "no rating"
        }else {
            "${business.value?.rating} / 5"
        }
    }

    val tableString = Transformations.map(_table) { table ->
        "Table: ${table}"
    }

    init {
        _overviewState.value = OverviewState.INITIAL
    }

    fun handleScan(scanResult: IntentResult) {
        if (scanResult.contents == null) {
            _overviewState.value = OverviewState.SCAN_FAIL
        } else {
            Timber.i("Scanned: %s", scanResult)
            val business = BusinessRepository.getBusinessByID(scanResult.contents)
            _table.value = 1 //todo get table info out of qr code
            switchBusiness(business)
        }
    }

    fun handleManual(code: String, table: Int) {
        Timber.i("Entered: %s", code)
        val business = BusinessRepository.getBusinessByCode(code)
        _table.value = table
        switchBusiness(business)
    }

    private fun switchBusiness(business: Business) {
        _business.value = business
        _overviewState.value = OverviewState.CODE_SUCCESS
    }

    fun orderNow() {
        if (_business.value == null) {
            Timber.e("No businessID when trying to order")
            return
        }
        if (business.value == null) {
            Timber.e("No business in binding when trying to order")
            return
        }
        if (business.value!!.isOpen())
            _navigationEvent.value = OverviewNavigationEvent.TO_MENU
        else
            _navigationEvent.value = OverviewNavigationEvent.TO_NOT_OPEN
    }

    fun resetNavigation() {
        _navigationEvent.value = OverviewNavigationEvent.NONE
    }
}

enum class OverviewState {
    INITIAL, CODE_SUCCESS, SCAN_FAIL, MANUAL_FAIL
}

enum class OverviewNavigationEvent {
    NONE, TO_MENU, TO_NOT_OPEN
}