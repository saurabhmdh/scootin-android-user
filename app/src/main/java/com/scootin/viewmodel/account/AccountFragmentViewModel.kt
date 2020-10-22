package com.scootin.viewmodel.account

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.scootin.repository.UserRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class AccountFragmentViewModel  @ViewModelInject
internal constructor(
    private val userRepository: UserRepository
) : ObservableViewModel() {

//    fun getAllTemples() = templeRepo.getAllTemples(viewModelScope.coroutineContext + Dispatchers.IO)

    val addNewAddress = MutableLiveData<Int>()

    fun addNewAddress(transaction: Int) {
        addNewAddress.postValue(transaction)
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    val addNewAddressLiveData = addNewAddress.switchMap {
        Timber.i("addNewAddress in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("addNewAddress in viewmodel 1")
            emit(userRepository.addNewAddress())
        }
    }

    val updateDefaultAddress = MutableLiveData<String>()

    fun updateDefaultAddress(addressId: String) {
        updateDefaultAddress.postValue(addressId)
    }

    val updateDefaultAddressLiveData = updateDefaultAddress.switchMap {
        Timber.i("addNewAddress in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("addNewAddress in viewmodel 1")
            emit(userRepository.updateDefaultAddress())
        }
    }
}
