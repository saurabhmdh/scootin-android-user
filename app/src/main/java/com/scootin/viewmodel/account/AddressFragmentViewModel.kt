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

class AddressFragmentViewModel @ViewModelInject
internal constructor(
    private val userRepository: UserRepository
) : ObservableViewModel() {

    val state = MutableLiveData<Int>()

    fun state(i: Int) {
        state.postValue(i)
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    val stateLiveData = state.switchMap {
        Timber.i("filePath in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            emit(userRepository.getAllState())
        }
    }

    val address = MutableLiveData<Int>()

    fun address(i: Int) {
        address.postValue(i)
    }

    val addressLiveData = address.switchMap {
        Timber.i("filePath in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            emit(userRepository.getAllAddress())
        }
    }
}
