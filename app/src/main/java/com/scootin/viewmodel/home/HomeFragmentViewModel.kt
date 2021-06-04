package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.database.dao.CacheDao
import com.scootin.database.table.Cache
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RequestFCM
import com.scootin.repository.CartRepository
import com.scootin.repository.CategoryRepository
import com.scootin.repository.UserRepository
import com.scootin.util.constants.AppConstants
import com.scootin.view.vo.ServiceArea
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class HomeFragmentViewModel @ViewModelInject
internal constructor(
    private val categoryRepository: CategoryRepository,
    private val cacheDao: CacheDao,
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository
) : ObservableViewModel(), CoroutineScope {

    val serviceArea = MutableLiveData<ServiceArea>()

    fun getHomeCategory()= categoryRepository.getHomeCategory(viewModelScope.coroutineContext + Dispatchers.IO)

    fun getAllServiceArea() = liveData(coroutineContext + handler) {
        emit(userRepository.getAllServiceArea())
    }

    fun saveServiceArea(id: ServiceArea) = liveData(coroutineContext + handler) {
        val previousServiceArea = cacheDao.getCacheData(AppConstants.USER_SERVICE_AREA)

        if (previousServiceArea?.value != null) {
            val listType = object : TypeToken<ServiceArea>() {}.type
            val prevServiceAreaInfo = Gson().fromJson<ServiceArea>(previousServiceArea.value, listType)

            if (prevServiceAreaInfo.id != id.id) {
                cartRepository.deleteCart(AppHeaders.userID)
            }
        }
        AppHeaders.serviceAreaId = id.id
        cacheDao.insert(Cache(AppConstants.SERVICE_AREA, AppHeaders.serviceAreaId.toString()))
        emit(cacheDao.insert(Cache(AppConstants.USER_SERVICE_AREA, Gson().toJson(id))))
    }

    fun getServiceArea() = cacheDao.getData(AppConstants.USER_SERVICE_AREA)


    fun updateServiceAreaDetail(serviceArea: ServiceArea) {
        launch {
            cacheDao.insert(Cache(AppConstants.SERVICE_AREA, serviceArea.id.toString()))
        }
        AppHeaders.serviceAreaId = serviceArea.id
    }

    fun updateMainCategory(selectedCategoryID: String?) {
        launch {
            selectedCategoryID?.let {
                cacheDao.insert(Cache(AppConstants.MAIN_CATEGORY, it))
                cacheDao.insert(Cache(AppConstants.SUB_CATEGORY, "-1"))
            }
        }
    }

    fun updateSubCategory(selectedCategoryID: String?) {
        launch {
            selectedCategoryID?.let {
                cacheDao.insert(Cache(AppConstants.SUB_CATEGORY, it))
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


    fun getCartCount(userId: String) = liveData(coroutineContext + handler) {
        emit(cartRepository.getCartCount(userId))
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO

}