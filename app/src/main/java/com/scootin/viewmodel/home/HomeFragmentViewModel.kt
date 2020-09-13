package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.scootin.repository.CategoryRepository
import com.scootin.repository.UserRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.Dispatchers
import java.security.PrivateKey

class HomeFragmentViewModel @ViewModelInject
internal constructor(
    private val categoryRepository: CategoryRepository
) : ObservableViewModel() {

//    fun getAllTemples() = templeRepo.getAllTemples(viewModelScope.coroutineContext + Dispatchers.IO)
  fun getHomeCategory()= categoryRepository.getHomeCategory(viewModelScope.coroutineContext + Dispatchers.IO)
}