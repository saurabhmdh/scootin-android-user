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
import com.scootin.repository.PaymentRepository
import com.scootin.repository.SearchRepository
import com.scootin.util.constants.AppConstants
import com.scootin.util.ui.FileUtils
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
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
        Timber.i("Saurabh add to cart $addToCartRequest")
        addToCartLiveData.postValue(addToCartRequest)
    }

    //Wrong code
//    val addToCartMap = addToCartLiveData.switchMap {
//        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
//            emit(searchRepository.addToCart(it))
//        }
//    }

    val addToCartMap = addToCartLiveData.asFlow().debounce(AppConstants.DEBOUNCE_TIME).mapLatest {
        searchRepository.addToCart(it)
    }.catch {
        Timber.i("Some error code")
    }.asLiveData()
}