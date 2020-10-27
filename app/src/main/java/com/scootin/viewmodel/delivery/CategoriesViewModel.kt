package com.scootin.viewmodel.delivery

import android.app.Application
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.extensions.orZero
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.*
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.repository.SearchRepository
import com.scootin.util.constants.AppConstants
import com.scootin.util.ui.FileUtils
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import timber.log.Timber
import java.io.File


class CategoriesViewModel @ViewModelInject internal constructor(
    private val cacheDao: CacheDao,
    private val locationDao: LocationDao,
    val searchRepository: SearchRepository,
    private val application: Application
) : ObservableViewModel() {

    private val _search = MutableLiveData<SearchShopsByCategory>()

    data class SearchShopsByCategory(val query: String)

    fun doSearch(query: String) {
        _search.postValue(SearchShopsByCategory(query))
    }

    val shops: LiveData<Response<List<SearchShopsByCategoryResponse>>> =
        _search.switchMap { search ->

            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
                Timber.i("Search Detail ${search.query}")
                val locationInfo = locationDao.getEntityLocation()
                val mainCategory = cacheDao.getCacheData(AppConstants.MAIN_CATEGORY)?.value
                val serviceArea = cacheDao.getCacheData(AppConstants.SERVICE_AREA)?.value

                val request =
                    RequestSearch(locationInfo.longitude, locationInfo.latitude, search.query)
                emit(
                    searchRepository.searchShops(
                        request,
                        serviceArea.orEmpty(),
                        mainCategory.orEmpty()
                    )
                )
            }
        }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    val product: LiveData<Response<List<SearchProductsByCategoryResponse>>> =
        _search.switchMap { search ->

            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
                Timber.i("Search Detail ${search.query}")
                val mainCategory = cacheDao.getCacheData(AppConstants.MAIN_CATEGORY)?.value
                val serviceArea = cacheDao.getCacheData(AppConstants.SERVICE_AREA)?.value

                emit(
                    searchRepository.searchProducts(
                        search.query,
                        serviceArea.orEmpty(),
                        mainCategory.orEmpty()
                    )
                )
            }
        }

    val addToCartLiveData = MutableLiveData<AddToCartRequest>()

    fun addToCart(addToCartRequest: AddToCartRequest) {
        addToCartLiveData.postValue(addToCartRequest)
    }

    val addToCartMap = addToCartLiveData.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            emit(searchRepository.addToCart(it))
        }
    }

    val getUserCartList = MutableLiveData<String>()

    fun userCartList() {
        getUserCartList.postValue(AppHeaders.userID)
    }

    val getUserCartListLiveData = getUserCartList.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            emit(searchRepository.getUserCartList(it))
        }
    }

    val addMoney = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        Timber.i("addMoney in viewmodel 1")
        emit(searchRepository.addMoney())
    }

    val listTransaction = MutableLiveData<Int>()

    fun listTransaction(transaction: Int) {
        listTransaction.postValue(transaction)
    }

    val filePath = MutableLiveData<Uri>()

    fun filePath(uri: Uri) {
        filePath.postValue(uri)
    }

    val filePathLiveData = filePath.switchMap {
        Timber.i("filePath in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("filePath in viewmodel 1")
            val filePath = FileUtils.getPath(application, it)
            val file = File(filePath)
            val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val filePart =
                MultipartBody.Part.createFormData("media", file.name, requestBody)
            emit(searchRepository.uploadImage(filePart))
        }
    }

    val listTransactionLiveData = listTransaction.switchMap {
        Timber.i("listTransaction in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("listTransaction in viewmodel 1")
            emit(searchRepository.listTransaction())
        }
    }

    val placeOrder = MutableLiveData<PlaceOrderRequest>()

    fun placeOrder(request: PlaceOrderRequest) {
        placeOrder.postValue(request)
    }

    val placeOrderLiveData = placeOrder.switchMap {
        Timber.i("placeOrder in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("placeOrder in viewmodel 1")
            // TODO need to change
            emit(searchRepository.placeOrder(it))
        }
    }

    val orderRequest = MutableLiveData<Map<String, String>>()

    fun orderRequest(request: Map<String, String>) {
        orderRequest.postValue(request)
    }

    val orderRequestLiveData = orderRequest.switchMap {
        Timber.i("orderRequest in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("orderRequest in viewmodel 1 ${ it.get("paymentMode").toString()} : orderid : ${it.get("orderId")?.toInt().orZero()}" )
            val orderRequest = OrderRequest(paymentMode = it.get("paymentMode").toString())
            emit(searchRepository.userConfirmOrder(it.get("orderId")?.toInt().orZero(), orderRequest))
        }
    }

    val promCodeRequest = MutableLiveData<Map<String, String>>()

    fun promCodeRequest(request: Map<String, String>) {
        promCodeRequest.postValue(request)
    }

    val promCodeRequestLiveData = promCodeRequest.switchMap {
        Timber.i("promCodeRequest in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("promCodeRequest in viewmodel 1")
            val promoCodeRequest = PromoCodeRequest(it.get("promocode").toString())
            emit(
                searchRepository.applyPromoCode(it.get("orderID")?.toInt().orZero(),
                    promoCodeRequest
                )
            )
        }
    }

    val verifyPaymentRequest = MutableLiveData<VerifyAmountRequest>()

    fun verifyPaymentRequest(request: VerifyAmountRequest) {
        verifyPaymentRequest.postValue(request)
    }

    val verifyPaymentRequestLiveData = verifyPaymentRequest.switchMap {
        Timber.i("verifyPaymentRequest in viewmodel")
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("verifyPaymentRequest in viewmodel 1")
            // TODO need to change
            emit(searchRepository.verifyPayment(it))
        }
    }
}