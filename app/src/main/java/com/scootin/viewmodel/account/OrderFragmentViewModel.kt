package com.scootin.viewmodel.account


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.scootin.network.request.RequestHistory
import com.scootin.repository.OrderRepository
import com.scootin.repository.UserRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class OrderFragmentViewModel  @ViewModelInject
internal constructor(
    private val orderRepository: OrderRepository
) : ObservableViewModel() {

//    fun getAllTemples() = templeRepo.getAllTemples(viewModelScope.coroutineContext + Dispatchers.IO)

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    fun getAllOrdersForUser(userId:String) =
        orderRepository.getAllOrdersForUser(
            userId).cachedIn(viewModelScope).asLiveData()

    fun getDirectOrder(orderId: String) =
        orderRepository.getDirectOrder(orderId,viewModelScope.coroutineContext + Dispatchers.IO + handler)

    fun getCityWideOrder(orderId: String) =
        orderRepository.getCityWideOrder(orderId,viewModelScope.coroutineContext + Dispatchers.IO + handler)

    fun getOrder(orderId: String) =
        orderRepository.getOrder(orderId,viewModelScope.coroutineContext + Dispatchers.IO + handler)

}
