package com.scootin.viewmodel.delivery

import android.app.Application
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.extensions.DoubleTrigger
import com.scootin.extensions.orZero
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.*
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.network.response.cart.CartListResponseItem
import com.scootin.repository.CartRepository
import com.scootin.repository.PaymentRepository
import com.scootin.repository.SearchRepository
import com.scootin.util.constants.AppConstants
import com.scootin.util.ui.FileUtils
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import timber.log.Timber
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext


class CategoriesViewModel @ViewModelInject internal constructor(
    private val cacheDao: CacheDao,
    private val locationDao: LocationDao,
    val searchRepository: SearchRepository,
    private val application: Application,
    private val cartRepository: CartRepository
) : ObservableViewModel(), CoroutineScope {

    private val _search = MutableLiveData<SearchShopsByCategory>()

    private val _selectedShop = MutableLiveData<Long>()

    fun updateShop(shopId: Long) {
        _selectedShop.postValue(shopId)
    }

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
                    RequestSearch(locationInfo.latitude, locationInfo.longitude, search.query)
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
        DoubleTrigger<SearchShopsByCategory, Long>(_search, _selectedShop).switchMap {pair ->
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
                Timber.i("Search Detail ${pair.first?.query}  second ${pair.second.orZero()}")
                val mainCategory = cacheDao.getCacheData(AppConstants.MAIN_CATEGORY)?.value
                val serviceArea = cacheDao.getCacheData(AppConstants.SERVICE_AREA)?.value
                val k = pair.second.orZero()
                if (k == 0L) {
                    emit(searchRepository.searchProducts(pair.first?.query.orEmpty(), serviceArea.orEmpty(), mainCategory.orEmpty()))
                } else {
                    // -- When user select a shop
                    emit(searchRepository.findProductFromShop(pair.second.orZero() , pair.first?.query.orEmpty()))
                }
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

    fun loadCount() {
        countUserCartListTotal.postValue(true)
    }
    private val countUserCartListTotal = MutableLiveData<Boolean>()


    val getCartCount = countUserCartListTotal.switchMap {
        liveData(coroutineContext + handler) {
            emit(cartRepository.getCartCount(AppHeaders.userID))
        }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO
}