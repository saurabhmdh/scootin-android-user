package com.scootin.viewmodel.payment


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*

import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddToCartRequest
import com.scootin.network.request.OrderRequest
import com.scootin.network.request.PromoCodeRequest
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.network.response.placeOrder.PlaceOrderResponse
import com.scootin.repository.OrderRepository
import com.scootin.repository.PaymentRepository
import com.scootin.repository.SearchRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import timber.log.Timber


class PaymentViewModel @ViewModelInject internal constructor(
    private val orderRepository: OrderRepository,
    val searchRepository: SearchRepository,
    private val paymentRepository: PaymentRepository
) : ObservableViewModel() {

    private val _Order_Id = MutableLiveData<Long>()

    fun loadOrder(orderId: Long) {
        _Order_Id.postValue(orderId)
    }

    val orderInfo = _Order_Id.switchMap {
        orderRepository.getOrder(it.toString(),viewModelScope.coroutineContext + Dispatchers.IO + handler)
    }

    fun promCodeRequest(orderId: String, promoCode: String): LiveData<Response<PlaceOrderResponse>> {
        return liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            val promoCodeRequest = PromoCodeRequest(promoCode)
            emit(paymentRepository.applyPromoCode(orderId, AppHeaders.userID, promoCodeRequest))
        }
    }

    fun userConfirmOrder(orderId: String, userId: String, orderRequest: OrderRequest) = orderRepository.userConfirmOrder(orderId, userId, orderRequest, viewModelScope.coroutineContext + Dispatchers.IO + handler)


    fun verifyPayment(request: VerifyAmountRequest) = paymentRepository.verifyPayment(request, viewModelScope.coroutineContext + Dispatchers.IO + handler)


    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }
}