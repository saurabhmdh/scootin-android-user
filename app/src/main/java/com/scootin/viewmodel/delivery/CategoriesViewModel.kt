package com.scootin.viewmodel.delivery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.database.table.Cache
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.*
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.network.response.cart.CartListResponseItem
import com.scootin.repository.CartRepository
import com.scootin.repository.SearchRepository
import com.scootin.util.constants.AppConstants
import com.scootin.view.vo.ProductSearchVO
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import timber.log.Timber
import kotlin.coroutines.CoroutineContext


class CategoriesViewModel @ViewModelInject internal constructor(
    private val cacheDao: CacheDao,
    private val locationDao: LocationDao,
    val searchRepository: SearchRepository,
    private val cartRepository: CartRepository
) : ObservableViewModel(), CoroutineScope {

    private val _search = MutableLiveData<SearchShopsByCategory>()

    private val _selectedShop = MutableLiveData<Long>()

    private val _search_by_shop = MutableLiveData<SearchShopsItem>()

    fun updateShop(shopId: Long) {
        _selectedShop.postValue(shopId)
    }

    data class SearchShopsByCategory(val query: String)

    fun doSearch(query: String) {
        _search.postValue(SearchShopsByCategory(query))
    }

    fun doSearchByShop(query: String, shopId: Long) {
        _search_by_shop.postValue(SearchShopsItem(query, shopId))
    }

    fun updateSubCategory(selectedCategoryID: String?) {
        launch {
            selectedCategoryID?.let {
                cacheDao.insert(Cache(AppConstants.SUB_CATEGORY, it))
            }
        }
    }

    data class SearchShopsItem(val query: String, val shopId: Long)

    val shops: LiveData<PagingData<SearchShopsByCategoryResponse>> =
        _search.switchMap { search ->
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
                Timber.i("Search Detail ${search.query}")
                val locationInfo = locationDao.getEntityLocation()
                val mainCategory = cacheDao.getCacheData(AppConstants.MAIN_CATEGORY)?.value
                val serviceArea = cacheDao.getCacheData(AppConstants.SERVICE_AREA)?.value

                val request =
                    RequestSearch(locationInfo.latitude, locationInfo.longitude, search.query)
                emitSource(
                    searchRepository.searchShops(
                        request,
                        serviceArea.orEmpty(),
                        mainCategory.orEmpty()
                    ).cachedIn(viewModelScope).asLiveData()
                )
            }
        }


    val shopsBySubcategory: LiveData<PagingData<SearchShopsByCategoryResponse>> =
        _search.switchMap { search ->
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
                Timber.i("Search Detail ${search.query}")
                val locationInfo = locationDao.getEntityLocation()
                val mainCategory = cacheDao.getCacheData(AppConstants.SUB_CATEGORY)?.value
                val serviceArea = cacheDao.getCacheData(AppConstants.SERVICE_AREA)?.value

                val request = RequestSearch(locationInfo.latitude, locationInfo.longitude, search.query)
                emitSource(
                    searchRepository.searchShopsBySubCategory(
                        request,
                        serviceArea.orEmpty(),
                        mainCategory.orEmpty()
                    ).cachedIn(viewModelScope).asLiveData()
                )
            }
        }


    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    val allProductByShop: LiveData<PagingData<ProductSearchVO>> = _search_by_shop.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler)  {
            Timber.i("Saurabh ${it.query} for shop ${it.shopId}")
            val mainCategory = cacheDao.getCacheData(AppConstants.MAIN_CATEGORY)?.value?.toLongOrNull() ?: -1
            val subCategory = cacheDao.getCacheData(AppConstants.SUB_CATEGORY)?.value?.toLongOrNull() ?: -1
            emitSource(searchRepository.findProductFromShopWithCategoryAndSubCategory(it.query,  it.shopId, mainCategory, subCategory).cachedIn(viewModelScope).asLiveData())
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

    val allProduct: LiveData<PagingData<ProductSearchVO>> = _search.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler)  {
            val mainCategory = cacheDao.getCacheData(AppConstants.MAIN_CATEGORY)?.value
            val serviceArea = cacheDao.getCacheData(AppConstants.SERVICE_AREA)?.value
            emitSource(searchRepository.findProductsWithPaging(it.query,  serviceArea.orEmpty(), mainCategory.orEmpty()).cachedIn(viewModelScope).asLiveData())
        }
    }

    val allProductBySubCategory: LiveData<PagingData<ProductSearchVO>> = _search.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler)  {
            val subCategory = cacheDao.getCacheData(AppConstants.SUB_CATEGORY)?.value
            val serviceArea = cacheDao.getCacheData(AppConstants.SERVICE_AREA)?.value
            emitSource(searchRepository.searchProductBySubCategories(it.query,  serviceArea.orEmpty(), subCategory.orEmpty()).cachedIn(viewModelScope).asLiveData())
        }
    }

    //for now lets not imagine the filter
    val allProductBySubCategoryWithFilter: LiveData<PagingData<ProductSearchVO>> = _search.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler)  {
            val subCategory = cacheDao.getCacheData(AppConstants.SUB_CATEGORY)?.value
            val serviceArea = cacheDao.getCacheData(AppConstants.SERVICE_AREA)?.value

            emitSource(searchRepository.findProductsBySubCategoryWithFilters(serviceArea.orEmpty(), subCategory.orEmpty(),
                RequestSearchWithFilter(query = it.query)).cachedIn(viewModelScope).asLiveData())
        }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO
}