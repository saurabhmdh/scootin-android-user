package com.scootin.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.network.request.RequestSearch
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.repository.SearchRepository
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import timber.log.Timber

class StationeryViewModel @ViewModelInject internal constructor(
    private val cacheDao: CacheDao,
    private val locationDao: LocationDao,
    searchRepository: SearchRepository
) : ObservableViewModel() {

    private val _search = MutableLiveData<SearchShopsByCategory>()

    data class SearchShopsByCategory(val query: String)

    fun doSearch(query: String) {
        _search.postValue(SearchShopsByCategory(query))
    }

    val shops: LiveData<Response<List<SearchShopsByCategoryResponse>>> = _search.switchMap { search ->

        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("Search Detail ${search.query}")
            val locationInfo = locationDao.getEntityLocation()
            val mainCategory = cacheDao.getCacheData(AppConstants.MAIN_CATEGORY)?.value
            val serviceArea = cacheDao.getCacheData(AppConstants.SERVICE_AREA)?.value

            val request = RequestSearch(locationInfo.longitude, locationInfo.latitude,search.query)
            emit(searchRepository.searchShops(request, serviceArea.orEmpty(), mainCategory.orEmpty()))
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    val product: LiveData<Response<List<SearchProductsByCategoryResponse>>> = _search.switchMap { search ->

        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("Search Detail ${search.query}")
            val mainCategory = cacheDao.getCacheData(AppConstants.MAIN_CATEGORY)?.value
            val serviceArea = cacheDao.getCacheData(AppConstants.SERVICE_AREA)?.value

            emit(searchRepository.searchProducts(search.query, serviceArea.orEmpty(), mainCategory.orEmpty()))
        }
    }
}