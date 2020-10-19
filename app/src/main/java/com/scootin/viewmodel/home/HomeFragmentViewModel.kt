package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.database.table.Cache
import com.scootin.database.table.EntityLocation
import com.scootin.network.api.APIService
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RequestFCM
import com.scootin.network.response.login.ResponseUser
import com.scootin.repository.CategoryRepository
import com.scootin.repository.UserRepository
import com.scootin.util.constants.AppConstants
import com.scootin.view.vo.ServiceArea
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class HomeFragmentViewModel @ViewModelInject
internal constructor(
    private val categoryRepository: CategoryRepository,
    private val apiService: APIService,
    private val locationDao: LocationDao,
    private val cacheDao: CacheDao,
    private val userRepository: UserRepository
) : ObservableViewModel(), CoroutineScope {

    val presentLocation = locationDao.getCurrentLocation()

    val serviceArea = MutableLiveData<ServiceArea>()

    val serviceAreaError = MutableLiveData<Boolean>()

    fun getHomeCategory()= categoryRepository.getHomeCategory(viewModelScope.coroutineContext + Dispatchers.IO)

    fun findServiceArea(latitude: Double, longitude: Double) {
        launch(coroutineContext) {
            Timber.i("Now finding the location $latitude, $longitude")
            val response = apiService.findServiceArea(mapOf("latitude" to latitude.toString(), "longitude" to longitude.toString()))
            if (response.isSuccessful) {
                val result = response.body()
                Timber.i("We find decide your service area = ${result}")

                if (result == null) {
                    serviceAreaError.postValue(true)
                } else {
                    cacheDao.insert(Cache(AppConstants.SERVICE_AREA, result.id.toString()))
                    serviceArea.postValue(ServiceArea(result.id, result.name))
                }
            } else {
                serviceAreaError.postValue(true)
            }
        }
    }

    fun updateLocation(place: Place) {
        launch {
            locationDao.insert(EntityLocation(place))
        }
    }

    fun updateMainCategory(selectedCategoryID: String?) {
        launch {
            selectedCategoryID?.let {
                cacheDao.insert(Cache(AppConstants.MAIN_CATEGORY, it))
            }
        }
    }

    fun updateFCMID (token: String?) {
        //get current FCM ID and its not same as current, We will send to server..
        launch {
            token?.let {
                val cache = cacheDao.getCacheData(AppConstants.FCM_ID)

                if ((cache == null || cache.value != it ) && AppHeaders.userID.isEmpty().not()) {
                    Timber.i("Data which need to update to server user ${AppHeaders.userID} value $it")
                    userRepository.updateFCMId(AppHeaders.userID, RequestFCM(it))
                    cacheDao.insert(Cache(AppConstants.FCM_ID, it))
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO

}