package com.scootin.viewmodel.cart

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.PlaceOrderRequest
import com.scootin.repository.CartRepository
import com.scootin.repository.OrderRepository
import com.scootin.repository.PaymentRepository
import com.scootin.repository.SearchRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class CartViewModel @ViewModelInject internal constructor(
    val searchRepository: SearchRepository,
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
) : ObservableViewModel(), CoroutineScope {

    val getUserCartList = MutableLiveData<String>()

    fun userCartList() {
        getUserCartList.postValue(AppHeaders.userID)
    }

    val getUserCartListLiveData = getUserCartList.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            emit(searchRepository.getUserCartList(it))
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    fun getCartCount(userId: String) = liveData(coroutineContext + handler) {
        emit(cartRepository.getCartCount(userId))
    }

    fun getTotalPrice(userId: String) = paymentRepository.getTotalPriceFromCart(userId, viewModelScope.coroutineContext + Dispatchers.IO + handler)

    fun generateOrder(userId: String, placeOrderRequest: PlaceOrderRequest,) = orderRepository.placeOrder(userId, placeOrderRequest, viewModelScope.coroutineContext + Dispatchers.IO + handler)


    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO
}