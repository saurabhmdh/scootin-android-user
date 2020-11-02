package com.scootin.viewmodel.order

import android.app.Application
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.network.request.DirectOrderRequest
import com.scootin.repository.OrderRepository
import com.scootin.repository.SearchRepository
import com.scootin.util.ui.FileUtils
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import kotlin.coroutines.CoroutineContext


class DirectOrderViewModel @ViewModelInject internal constructor(
    private val orderRepository: OrderRepository,
    val searchRepository: SearchRepository,
    private val application: Application
) : ObservableViewModel() {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
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

    fun placeDirectOrder(userId: String, request: DirectOrderRequest) = orderRepository.placeDirectOrder(userId, request, viewModelScope.coroutineContext + Dispatchers.IO + handler)
}