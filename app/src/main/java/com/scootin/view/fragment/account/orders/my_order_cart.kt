package com.scootin.view.fragment.account.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentMyOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.OrderFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class my_order_cart : Fragment(R.layout.fragment_my_orders) {

    private var binding by autoCleared<FragmentMyOrdersBinding>()
    private val viewModel: OrderFragmentViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyOrdersBinding.bind(view)

        updateListeners()
    }

    private fun updateListeners(){


    }
}