package com.scootin.view.fragment.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentOrderHistoryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.order.OrderHistoryItem
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.OrderHistoryAdapter
import com.scootin.viewmodel.account.OrderFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class OrderHistoryFragment : Fragment(R.layout.fragment_order_history) {
    private var binding by autoCleared<FragmentOrderHistoryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private val viewModel: OrderFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderHistoryBinding.bind(view)
        setAdaper()
        setViewModel()
        //setupListener()
    }

    private fun setViewModel() {
        viewModel.getAllOrdersForUser(AppHeaders.userID).observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                orderHistoryAdapter.submitData(it)
            }
        }
    }

//    private fun setupListener() {
//        binding.back.setOnClickListener { findNavController().popBackStack() }
//    }

    private fun setAdaper() {
        orderHistoryAdapter =
            OrderHistoryAdapter(
                appExecutors,
                object : OrderHistoryAdapter.ImageAdapterClickLister {
                    override fun onViewDetailsSelected(view: View, item: OrderHistoryItem) {
                        when (item.orderType){
                            "NORMAL"-> {
                                findNavController().navigate(
                                    OrderHistoryFragmentDirections.inorderToTrackFragment(
                                        item.orderId.toString()
                                    )
                                )
                            }
                            "DIRECT"->{
                                findNavController().navigate(
                                    OrderHistoryFragmentDirections.orderToTrackFragment(
                                        item.orderId.toString()
                                    )
                                )
                            }
                            "CITYWIDE"->{
                                findNavController().navigate(
                                    OrderHistoryFragmentDirections.citywideToTrackFragment(
                                        item.orderId.toString()
                                    )
                                )
                            }
                        }

                    }
                })

        binding.orderList.apply {
            adapter = orderHistoryAdapter
        }
    }
}