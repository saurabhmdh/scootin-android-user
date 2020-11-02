package com.scootin.view.fragment.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentOrderHistoryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.response.order.OrderHistoryItem
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.OrderHistoryAdapter
import com.scootin.viewmodel.account.OrderFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
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
        setupListener()
//        orderHistoryAdapter.submitList(setList())
    }

    private fun setViewModel() {
        viewModel.getAllOrdersForUser().observe(viewLifecycleOwner, Observer {
            Timber.i("response status = ${it.status}")
            when (it.status) {
                Status.SUCCESS -> {
                    val responseList = it.data
                    Timber.i("response list size = ${responseList?.size}")
                    orderHistoryAdapter.submitList(responseList)
                }
                else -> {
                    Timber.e("message = ${it.message}")
                }
            }
        })
    }

    private fun setupListener() {
        binding.back.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setAdaper() {
        orderHistoryAdapter =
            OrderHistoryAdapter(
                appExecutors,
                object : OrderHistoryAdapter.ImageAdapterClickLister {
                    override fun onViewDetailsSelected(view: View, item: OrderHistoryItem) {
                        if (item.directOrder) {
                            findNavController().navigate(
                                OrderHistoryFragmentDirections.orderToTrackFragment(
                                    item.id.toString()
                                )
                            )
                        }
                        else {
                            findNavController().navigate(
                                OrderHistoryFragmentDirections.inorderToTrackFragment(
                                    item.id.toString()
                                )
                            )
                        }
                    }
                })

        binding.orderList.apply {
            adapter = orderHistoryAdapter
        }
    }
}