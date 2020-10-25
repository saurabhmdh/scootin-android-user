package com.scootin.viewmodel.account

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.Address
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

    val addNewAddress = MutableLiveData<Address>()

    fun addNewAddress(address: Address) {
        addNewAddress.postValue(address)
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    val addNewAddressLiveData = addNewAddress.switchMap {
        Timber.i("addNewAddress in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("addNewAddress in viewmodel 1")
            // TODO add new Address
            emit(userRepository.addNewAddress(it))
        }
    }

    val updateDefaultAddress = MutableLiveData<String>()

    fun updateDefaultAddress(addressId: String) {
        updateDefaultAddress.postValue(addressId)
    }

    val updateDefaultAddressLiveData = updateDefaultAddress.switchMap {
        Timber.i("updateDefaultAddress in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("updateDefaultAddress in viewmodel 1")
            // TODO get addressId
            emit(userRepository.updateDefaultAddress(it))
        }
    }

    val getAllAddress = MutableLiveData<Int>()

    fun getAllAddress(value: Int) {
        getAllAddress.postValue(value)
    }

    val getAllAddressLiveData = getAllAddress.switchMap {
        Timber.i("getAllAddress in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("getAllAddress in viewmodel 1")
            emit(userRepository.getAllAddress())
        }
    }
}
