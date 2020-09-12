package com.scootin.viewmodel.account

import androidx.hilt.lifecycle.ViewModelInject
import com.scootin.repository.UserRepository
import com.scootin.viewmodel.base.ObservableViewModel

class MyOrderCartViewModel  @ViewModelInject
internal constructor(
    private val userRepository: UserRepository
) : ObservableViewModel() {

//    fun getAllTemples() = templeRepo.getAllTemples(viewModelScope.coroutineContext + Dispatchers.IO)

}