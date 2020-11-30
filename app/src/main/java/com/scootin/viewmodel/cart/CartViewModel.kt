package com.scootin.viewmodel.cart


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddToCartRequest
import com.scootin.network.request.PlaceOrderRequest
import com.scootin.network.response.cart.CartListResponseItem
import com.scootin.repository.CartRepository
import com.scootin.repository.OrderRepository
import com.scootin.repository.PaymentRepository
import com.scootin.repository.SearchRepository
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.*
import retrofit2.Response
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
        Timber.i("userCartList()... ")
        getUserCartList.postValue(AppHeaders.userID)
    }

    val getUserCartListLiveData = getUserCartList.switchMap {
        Timber.i("Its changed..")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            emit(searchRepository.getUserCartList(it))
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    fun loadCount() {
        countUserCartListTotal.postValue(true)
    }
    private val countUserCartListTotal = MutableLiveData<Boolean>()


    val getCartCount = countUserCartListTotal.switchMap {
        liveData(coroutineContext + handler) {
            emit(cartRepository.getCartCount(AppHeaders.userID))
        }
    }

    val totalPrice = getUserCartList.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            emit(paymentRepository.getTotalPriceFromCart(AppHeaders.userID))
        }
    }

    val addToCartLiveData = MutableLiveData<AddToCartRequest>()

    fun addToCart(addToCartRequest: AddToCartRequest) {
        addToCartLiveData.postValue(addToCartRequest)
    }

    private var job: Deferred<Unit>? = null
    val addToCartMap = addToCartLiveData.switchMap { cartData ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            job?.cancel()
            var data: Response<CartListResponseItem>? = null
            job = CoroutineScope(Dispatchers.IO).async {
                delay(AppConstants.DEBOUNCE_TIME)
                data = cartRepository.updateCart(cartData)
            }
            job?.await()
            emit(data)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO
}