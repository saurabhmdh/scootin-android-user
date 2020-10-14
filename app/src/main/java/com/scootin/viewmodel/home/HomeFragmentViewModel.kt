package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.scootin.database.dao.LocationDao
import com.scootin.database.table.EntityLocation
import com.scootin.network.api.APIService
import com.scootin.repository.CategoryRepository
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
    private val locationDao: LocationDao
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
    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO

}