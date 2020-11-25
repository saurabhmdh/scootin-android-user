package com.scootin.viewmodel.account


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.scootin.repository.OrderRepository
import com.scootin.repository.UserRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class SupportFragmentViewModel  @ViewModelInject
internal constructor(
    private val orderRepository: OrderRepository
) : ObservableViewModel() {

    fun verifyOrderId(orderId: String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        emit(orderRepository.checkOrder(orderId))
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

}