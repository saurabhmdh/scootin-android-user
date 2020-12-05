package com.scootin.viewmodel.order

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.scootin.database.dao.CacheDao
import com.scootin.network.request.CityWideOrderRequest
import com.scootin.network.request.DirectOrderRequest
import com.scootin.network.request.DistanceMeasure
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.ExtraDataItem
import com.scootin.network.response.Media
import com.scootin.repository.OrderRepository
import com.scootin.repository.SearchRepository
import com.scootin.repository.UserRepository
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File


class DirectOrderViewModel @ViewModelInject internal constructor(
    private val orderRepository: OrderRepository,
    val searchRepository: SearchRepository,
    private val cacheDao: CacheDao,
    private val userRepository: UserRepository
) : ObservableViewModel() {

    //Save the value..
    var list = ArrayList<ExtraDataItem>()

    var pickupAddress: AddressDetails? = null
    var dropAddress: AddressDetails? = null
    var media: Media? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    fun uploadMedia(file: File) = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        val filePart = MultipartBody.Part.createFormData("multipartFile", file.name, file.asRequestBody())
        Timber.i("Media uploadMedia")
        emit(searchRepository.uploadImage(filePart))
    }

    fun placeDirectOrder(userId: String, request: DirectOrderRequest) = orderRepository.placeDirectOrder(userId, request, viewModelScope.coroutineContext + Dispatchers.IO + handler)


    fun placeCityWideOrder(
        userId: String,
        deliveryAddressDetails: Long,
        pickupAddressDetails: Long,
        mediaId: Long) = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        val serviceArea = cacheDao.getCacheData(AppConstants.SERVICE_AREA)?.value
        emit(orderRepository.placeCityWideOrder(userId, CityWideOrderRequest(deliveryAddressDetails, pickupAddressDetails, mediaId, serviceArea?.toLong() ?: 0, null)))
    }

    fun loadAllAddress() = liveData(viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        emit(userRepository.getAllAddress())
    }

    fun findDistance(request: DistanceMeasure) = liveData(viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        emit(userRepository.findDistance(request))
    }

}