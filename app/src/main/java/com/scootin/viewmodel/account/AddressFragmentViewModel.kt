package com.scootin.viewmodel.account


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.scootin.repository.SearchRepository
import com.scootin.repository.UserRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.util.*

class AddressFragmentViewModel @ViewModelInject
internal constructor(
    private val userRepository: UserRepository,
    private val searchRepository: SearchRepository
) : ObservableViewModel() {


    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    val addressLiveData = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            emit(userRepository.getAllAddress())
    }

    val deliverySlot = liveData(viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        emit(searchRepository.getDeliverySlot(Date().time))
    }
}
