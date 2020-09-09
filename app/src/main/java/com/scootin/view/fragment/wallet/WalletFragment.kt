package com.scootin.view.fragment.wallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentOrderHistoryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.orders.OrderHistoryList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order_history.OrderHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WalletFragment: Fragment(R.layout.fragment_wallet)  {

}