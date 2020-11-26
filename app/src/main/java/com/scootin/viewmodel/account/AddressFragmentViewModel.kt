package com.scootin.viewmodel.account


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.scootin.network.request.AddAddressRequest
import com.scootin.repository.SearchRepository
import com.scootin.repository.UserRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext

class AddressFragmentViewModel @ViewModelInject
internal constructor(
    private val userRepository: UserRepository,
    private val searchRepository: SearchRepository
) : ObservableViewModel(), CoroutineScope {


    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    val addressLiveData = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            emit(userRepository.getAllAddress())
    }

    val deliverySlot = liveData(viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        emit(searchRepository.getDeliverySlot(Date().time))
    }

    val stateInfo = userRepository.getAllState(viewModelScope.coroutineContext + Dispatchers.IO + handler)

    fun saveAddress(request: AddAddressRequest) = liveData(viewModelScope.coroutineContext + Dispatchers.IO + handler) { emit(userRepository.addNewAddress(request)) }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO + handler
}
