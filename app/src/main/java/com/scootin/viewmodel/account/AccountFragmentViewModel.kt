package com.scootin.viewmodel.account

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.scootin.network.response.AddressDetails
import com.scootin.repository.UserRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class AccountFragmentViewModel  @ViewModelInject
internal constructor(
    private val userRepository: UserRepository
) : ObservableViewModel() {

}
