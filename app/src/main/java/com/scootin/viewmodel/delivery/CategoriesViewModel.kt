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

    val _searchShop = MutableLiveData<SearchShopsByCategory>()

    class SearchShopsByCategory(categoryID: Int, query: String)

    fun doSearchShop(categoryID: Int, query: String) {
        _searchShop.postValue(SearchShopsByCategory(categoryID, query))
    }

    val shops: LiveData<Response<List<SearchShopsByCategoryResponse>>> = _searchShop.switchMap { searchDetail ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            Timber.i("Search Detail ${searchDetail}")
            val locationInfo = locationDao.getEntityLocation()
            val request = RequestSearch(locationInfo.longitude, locationInfo.latitude, "tea")
            emit(searchRepository.searchShops(request, "8052", "253"))
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }
}