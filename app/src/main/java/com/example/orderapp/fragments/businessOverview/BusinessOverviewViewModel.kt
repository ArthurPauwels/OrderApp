package com.example.orderapp.fragments.businessOverview

import android.app.Application
import androidx.lifecycle.*
import com.example.orderapp.ServiceLocator
import com.example.orderapp.data.database.BusinessDatabaseDAO
import com.example.orderapp.data.repositories.BusinessRepository
import com.example.orderapp.model.Business
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.*
import timber.log.Timber

class BusinessOverviewViewModel : ViewModel() {

    private val _business = MutableLiveData<Business>()
    val business: LiveData<Business> get() = _business

    private val _navigationEvent = MutableLiveData<OverviewNavigationEvent>()
    val navigationEvent: LiveData<OverviewNavigationEvent> get() = _navigationEvent

    private val _table = MutableLiveData<Int>()

    private val _overviewState = MutableLiveData<OverviewState>()
    val overviewState : LiveData<OverviewState> get() = _overviewState

    val ratingString :LiveData<String> = Transformations.map(business) { b -> getRatingString(b.rating) }

    val tableString : LiveData<String> = Transformations.map(_table) { table -> "Table: ${table}" }

    private val repository = ServiceLocator.repository()

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    init {
        _overviewState.value = OverviewState.INITIAL
    }

    private fun getRatingString(rating: Int?) : String{
        if (rating == null) {
            return "no rating"
        }else {
            return "${rating} / 5"
        }
    }

    fun handleScan(scanResult: IntentResult) {

        if (scanResult.contents == null) {
            _overviewState.value = OverviewState.SCAN_FAIL
        } else {
            Timber.i("Scanned: %s", scanResult)
            _table.value = 1 //todo get table info out of qr code
            uiScope.launch {
                switchBusiness(getBusinessFromRepository(scanResult.contents).value!!)
            }
        }

    }

    private suspend fun getBusinessFromRepository(id: String) : LiveData<Business>{
        return withContext(Dispatchers.IO){
            repository.getBusinessByID(id)
        }
    }

    fun handleManual(code: String, table: Int) {
        Timber.i("Entered: %s", code)
        val business = repository.getBusinessByCode(code)
        _table.value = table
        switchBusiness(business.value!!)
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}

enum class OverviewState {
    INITIAL, CODE_SUCCESS, SCAN_FAIL, MANUAL_FAIL
}

enum class OverviewNavigationEvent {
    NONE, TO_MENU, TO_NOT_OPEN
}