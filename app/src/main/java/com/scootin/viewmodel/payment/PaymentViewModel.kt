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
import com.scootin.repository.UserRepository
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import timber.log.Timber
import kotlin.coroutines.CoroutineContext


class PaymentViewModel @ViewModelInject internal constructor(
    private val orderRepository: OrderRepository,
    val searchRepository: SearchRepository,
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository

) : ObservableViewModel(), CoroutineScope {

    private val _promo_code = MutableLiveData<String>()
    private val _Order_Id = MutableLiveData<Long>()

    fun loadPaymentInfo(promoCode: String) {
        _promo_code.postValue(promoCode)
    }

    val paymentInfo = _promo_code.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            emit(orderRepository.checkOutOrder(AppHeaders.userID, PromoCodeRequest(it, "NORMAL")))
        }
    }

    fun loadOrder(orderId: Long) {
        _Order_Id.postValue(orderId)
    }

    val orderInfo = _Order_Id.switchMap {
        orderRepository.getOrder(
            it.toString(),
            viewModelScope.coroutineContext + Dispatchers.IO + handler
        )
    }
    val directOrderInfo = _Order_Id.switchMap {
        orderRepository.getDirectOrder(
            it.toString(),
            viewModelScope.coroutineContext + Dispatchers.IO + handler
        )
    }
    val citywideOrderInfo = _Order_Id.switchMap {
        orderRepository.getCityWideOrder(
            it.toString(),
            viewModelScope.coroutineContext + Dispatchers.IO + handler
        )
    }
    fun userConfirmOrder(userId: String, orderRequest: OrderRequest) = orderRepository.userConfirmOrder(userId, orderRequest, viewModelScope.coroutineContext + Dispatchers.IO + handler)


    fun verifyPayment(request: VerifyAmountRequest) = paymentRepository.verifyPayment(request, viewModelScope.coroutineContext + Dispatchers.IO + handler)
    fun verifyPaymentDirect(request: VerifyAmountRequest) = paymentRepository.verifyPaymentDirect(request, viewModelScope.coroutineContext + Dispatchers.IO + handler)

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    fun loadAllAddress() = liveData(viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        emit(userRepository.getAllAddress())
    }


    fun applyPromo(orderId: String, userId: String, promoCodeRequest: PromoCodeRequest) = liveData(viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        emit(paymentRepository.applyPromoCode(orderId, userId, promoCodeRequest))
    }


    fun userConfirmOrderDirect(userId: String, orderRequest: OrderRequest) = orderRepository.userConfirmOrderDirect(userId, orderRequest, viewModelScope.coroutineContext + Dispatchers.IO + handler)


    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO
}