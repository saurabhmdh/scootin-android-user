package com.scootin.viewmodel.account


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.scootin.network.request.CancelOrderRequest
import com.scootin.network.request.DirectOrderRequest
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

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }
    private val _Order_Id = MutableLiveData<String>()

    fun loadOrder(orderId: String) {
        _Order_Id.postValue(orderId)
    }

    val orderInfo = _Order_Id.switchMap {
        orderRepository.getOrder(
            it.toString(),
            viewModelScope.coroutineContext + Dispatchers.IO + handler
        )
    }

    fun cancelOrder(orderId: String,request: CancelOrderRequest) = orderRepository.userCancelOrder(orderId, request,viewModelScope.coroutineContext + Dispatchers.IO + handler)

    fun getAllOrdersForUser(userId:String) =
        orderRepository.getAllOrdersForUser(
            userId).cachedIn(viewModelScope).asLiveData()

    val directOrder = _Order_Id.switchMap {
        orderRepository.getDirectOrder(it.toString(),viewModelScope.coroutineContext + Dispatchers.IO + handler)
    }


    val cityWideOrder = _Order_Id.switchMap {
        orderRepository.getCityWideOrder(it,viewModelScope.coroutineContext + Dispatchers.IO + handler)
    }

}
