package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.scootin.repository.TempleRepo
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class HomeFragmentViewModel @ViewModelInject
internal constructor(
    private val templeRepo: TempleRepo
) : ObservableViewModel() {

    fun getAllTemples() = templeRepo.getAllTemples(viewModelScope.coroutineContext + Dispatchers.IO)

}