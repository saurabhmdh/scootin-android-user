package com.scootin.viewmodel.account

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.scootin.network.request.DirectOrderRequest
import com.scootin.repository.OrderRepository
import com.scootin.repository.UserRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class MyOrderCartViewModel  @ViewModelInject
internal constructor(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) : ObservableViewModel() {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }



}