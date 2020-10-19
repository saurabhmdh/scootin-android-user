package com.scootin.viewmodel.delivery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.network.api.APIService
import com.scootin.network.api.Resource
import com.scootin.network.request.RequestSearch
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.repository.SearchRepository
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import timber.log.Timber

class CategoriesViewModel @ViewModelInject internal constructor(
    private val cacheDao: CacheDao,
    private val locationDao: LocationDao,
    searchRepository: SearchRepository
) : ObservableViewModel() {

    private val _searchShop = MutableLiveData<SearchShopsByCategory>()

    data class SearchShopsByCategory(val query: String)

    fun doSearchShop(query: String) {
        _searchShop.postValue(SearchShopsByCategory(query))
    }

    val shops: LiveData<Response<List<SearchShopsByCategoryResponse>>> = _searchShop.switchMap { search ->

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
}