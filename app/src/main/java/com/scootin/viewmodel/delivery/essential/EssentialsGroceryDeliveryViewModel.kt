package com.scootin.viewmodel.delivery.essential

import androidx.hilt.lifecycle.ViewModelInject
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.network.api.APIService
import com.scootin.viewmodel.base.ObservableViewModel

class EssentialsGroceryDeliveryViewModel @ViewModelInject internal constructor(
    private val cacheDao: CacheDao,
    private val apiService: APIService,
    private val locationDao: LocationDao
) : ObservableViewModel() {


    //
}