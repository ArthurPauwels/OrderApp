package com.example.orderapp.fragments.businessOverview

import android.app.Application
import androidx.lifecycle.*
import com.example.orderapp.data.database.getDatabase
import com.example.orderapp.data.repositories.BusinessRepository
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.*
import timber.log.Timber
import java.net.ConnectException

class BusinessOverviewViewModel(application: Application) : AndroidViewModel(application) {

    private val _navigationEvent = MutableLiveData<OverviewNavigationEvent>()
    val navigationEvent: LiveData<OverviewNavigationEvent> get() = _navigationEvent

    private val _table = MutableLiveData<Int>()

    private val _overviewState = MutableLiveData<OverviewState>()
    val overviewState : LiveData<OverviewState> get() = _overviewState

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val repository = BusinessRepository(database)

    val business = repository.currentBusiness
    val ratingString :LiveData<String> = Transformations.map(business) { b -> getRatingString(b.rating) }
    val tableString : LiveData<String> = Transformations.map(_table) { table -> "Table: ${table}" }

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
        val contents = scanResult.contents.split(',')
        val id = contents.first()
        if (!scanResult.contents.contains(',')) {
            _overviewState.value = OverviewState.SCAN_FAIL
        } else {
            viewModelScope.launch {
                Timber.i("Scanned: %s", scanResult)
                _overviewState.value = OverviewState.NETWORK_BUSY
                _table.value = contents[1].trim().toInt()
                try {
                    repository.refreshCurrentBusinessByID(id)
                }
                catch (e: ConnectException){
                    Timber.w(e)
                    _overviewState.value = OverviewState.NETWORK_FAIL
                }
                _overviewState.value = OverviewState.CODE_SUCCESS
            }
        }
    }

    fun handleManual(code: String, table: Int?) {
        viewModelScope.launch {
            Timber.i("Entered: %s", code)
            _overviewState.value = OverviewState.NETWORK_BUSY
            try {
                repository.refreshCurrentBusinessByCode(code)
            }
            catch (e: ConnectException){
                Timber.w(e)
                _overviewState.value = OverviewState.NETWORK_FAIL
            }
            _table.value = table
            _overviewState.value = OverviewState.CODE_SUCCESS
        }
    }

    fun orderNow() {
        if (business.value == null) {
            Timber.e("No business in binding when trying to order")
            return
        }
        if (business.value!!.isOpen())
            _navigationEvent.value = OverviewNavigationEvent.TO_NOT_OPEN
        else
            _navigationEvent.value = OverviewNavigationEvent.TO_NOT_OPEN
    }

    fun resetNavigation() {
        _navigationEvent.value = OverviewNavigationEvent.NONE
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

enum class OverviewState {
    INITIAL, CODE_SUCCESS, SCAN_FAIL, MANUAL_FAIL, NETWORK_FAIL, NETWORK_BUSY
}

enum class OverviewNavigationEvent {
    NONE, TO_MENU, TO_NOT_OPEN
}