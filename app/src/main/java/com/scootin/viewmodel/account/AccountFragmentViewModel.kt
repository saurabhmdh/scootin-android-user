package com.scootin.viewmodel.account

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.scootin.database.dao.CacheDao
import com.scootin.extensions.MakeLiveData
import com.scootin.network.AppExecutors
import com.scootin.network.response.AddressDetails
import com.scootin.repository.UserRepository
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class AccountFragmentViewModel  @ViewModelInject
internal constructor(
    private val userRepository: UserRepository,
    private val cacheDao: CacheDao,
    private val appExecutors: AppExecutors
) : ObservableViewModel() {

    private val _doLogout = MutableLiveData<Boolean>()

    fun doLogout() {
        _doLogout.postValue(true)
    }

    val logoutComplete = Transformations.switchMap(_doLogout) { request->
        if (request) {
            appExecutors.diskIO().execute {
                cacheDao.delete(AppConstants.USER_INFO)
            }
        }
        MakeLiveData.create(true)
    }
}
