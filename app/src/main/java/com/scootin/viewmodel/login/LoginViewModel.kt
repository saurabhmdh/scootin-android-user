package com.scootin.viewmodel.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.scootin.database.dao.CacheDao
import com.scootin.database.table.Cache
import com.scootin.extensions.AbsentLiveData
import com.scootin.network.AppExecutors
import com.scootin.network.response.login.ResponseUser
import com.scootin.repository.UserRepository
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class LoginViewModel @ViewModelInject internal constructor(
    private val userRepo: UserRepository,
    private val cacheDao: CacheDao
) : ObservableViewModel(), CoroutineScope  {

    private val _doLogin = MutableLiveData<RequestLogin>()

    private val _sendOTP = MutableLiveData<String>()

    val loginComplete = Transformations.switchMap(_doLogin) { request->
        if (request.userName.isEmpty() || request.password.isEmpty()) {
            AbsentLiveData.create()
        } else {
            userRepo.doLogin(mapOf("user" to request.userName, "pwd" to request.password),
                viewModelScope.coroutineContext + Dispatchers.IO)
        }
    }

    val requestOTPComplete = Transformations.switchMap(_sendOTP) {
        userRepo.sendOTP(mapOf("mobileNo" to it), viewModelScope.coroutineContext + Dispatchers.IO)
    }


    fun doLogin(userName: String, password: String) {
        _doLogin.postValue(RequestLogin(userName, password))
    }

    fun saveUserInfo(data: ResponseUser) {
        launch{
            Timber.i("saveUserInfo $data")
            val userData = Gson().toJson(data)
            cacheDao.insert(Cache(AppConstants.USER_INFO, userData))
        }
    }

    data class RequestLogin(val userName: String, val password: String)

    fun sendOTP(mobileNumber: String) {
        _sendOTP.postValue(mobileNumber)
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO

//    fun requestOTP(mobileNumber: String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO){
//        emit(userRepo.sendOTP(mapOf("mobileNo" to mobileNumber)))
//    }

}