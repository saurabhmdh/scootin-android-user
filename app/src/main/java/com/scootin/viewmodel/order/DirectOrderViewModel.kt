package com.scootin.viewmodel.order

import android.app.Application
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
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
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File


class DirectOrderViewModel @ViewModelInject internal constructor(
    private val orderRepository: OrderRepository,
    val searchRepository: SearchRepository,
    private val application: Application
) : ObservableViewModel() {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    fun uploadMedia(data: Uri) = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        val filePath = FileUtils.getPath(application, data)
        val file = File(filePath)
        val filePart = MultipartBody.Part.createFormData("multipartFile", file.name, file.asRequestBody())
        emit(searchRepository.uploadImage(filePart))
    }

    fun placeDirectOrder(userId: String, request: DirectOrderRequest) = orderRepository.placeDirectOrder(userId, request, viewModelScope.coroutineContext + Dispatchers.IO + handler)
}