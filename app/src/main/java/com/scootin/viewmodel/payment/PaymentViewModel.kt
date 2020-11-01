package com.scootin.viewmodel.payment


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*

import com.scootin.network.manager.AppHeaders
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



    fun promCodeRequest(orderId: String, promoCode: String): LiveData<Response<PlaceOrderResponse>> {
        return liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            val promoCodeRequest = PromoCodeRequest(promoCode)
            emit(paymentRepository.applyPromoCode(orderId, AppHeaders.userID, promoCodeRequest))
        }
    }

    fun userConfirmOrder(orderId: String, userId: String, orderRequest: OrderRequest) = orderRepository.userConfirmOrder(orderId, userId, orderRequest, viewModelScope.coroutineContext + Dispatchers.IO + handler)

    val verifyPaymentRequest = MutableLiveData<VerifyAmountRequest>()

    fun verifyPaymentRequest(request: VerifyAmountRequest) {
        verifyPaymentRequest.postValue(request)
    }

    val verifyPaymentRequestLiveData = verifyPaymentRequest.switchMap {
        Timber.i("verifyPaymentRequest in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("verifyPaymentRequest in viewmodel 1")
            // TODO need to change
            emit(paymentRepository.verifyPayment(it))
        }
    }


    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }
}