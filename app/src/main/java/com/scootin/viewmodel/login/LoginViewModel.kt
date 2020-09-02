package com.scootin.viewmodel.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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
import kotlinx.coroutines.Dispatchers

class LoginViewModel @ViewModelInject internal constructor(
    private val userRepo: UserRepository,
    private val cacheDao: CacheDao,
    private val executors: AppExecutors
) : ObservableViewModel()  {

    private val _doLogin = MutableLiveData<RequestLogin>()

    val loginComplete = Transformations.switchMap(_doLogin) { request->
        if(request.userName.isEmpty() || request.password.isEmpty()) {
            AbsentLiveData.create()
        } else {
            userRepo.doLogin(mapOf("user" to request.userName, "pwd" to request.password),
                viewModelScope.coroutineContext + Dispatchers.IO)
        }
    }

    fun doLogin(userName: String, password: String) {
        _doLogin.postValue(RequestLogin(userName, password))
    }

    fun saveUserInfo(data: ResponseUser?) {
        data?.let {
            executors.networkIO().execute {
                val userData = Gson().toJson(it)
                cacheDao.insert(Cache(AppConstants.USER_INFO, userData))
            }
        }
    }

    data class RequestLogin(val userName: String, val password: String)
}