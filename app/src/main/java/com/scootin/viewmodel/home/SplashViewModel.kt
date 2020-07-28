package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.Dispatchers

class SplashViewModel @ViewModelInject internal constructor() : ObservableViewModel() {

    fun firstLaunch() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        //If preference there then first or no..

        emit(true)
    }
}