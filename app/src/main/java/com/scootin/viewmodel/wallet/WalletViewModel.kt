package com.scootin.viewmodel.wallet


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.scootin.repository.WalletRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import timber.log.Timber


class WalletViewModel @ViewModelInject internal constructor(
    private val walletRepository: WalletRepository
) : ObservableViewModel() {

    fun listTransaction(userId: String) = walletRepository.listTransaction(userId, viewModelScope.coroutineContext + Dispatchers.IO + handler)

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

}