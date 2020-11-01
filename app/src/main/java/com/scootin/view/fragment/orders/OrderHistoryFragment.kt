package com.scootin.view.fragment.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentOrderHistoryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.orders.OrderHistoryList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.OrderHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderHistoryFragment: Fragment(R.layout.fragment_order_history) {
    private var binding by autoCleared<FragmentOrderHistoryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderHistoryBinding.bind(view)
        setAdaper()

        setupListener()
        orderHistoryAdapter.submitList(setList())

    }

    private fun setupListener() {
        binding.back.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setAdaper() {
        orderHistoryAdapter =
            OrderHistoryAdapter(
                appExecutors,
                object : OrderHistoryAdapter.ImageAdapterClickLister {
                    override fun onViewDetailsSelected(view: View) {

                    }


                })

        binding.orderList.apply {
            adapter = orderHistoryAdapter
        }
    }
    private fun setList(): ArrayList<OrderHistoryList> {
        val list = ArrayList<OrderHistoryList>()
        list.add(
            OrderHistoryList(
                0,
                "#0720280002",
                "Sweets",
                "20.08.2020",
                "Ongoing",
                "Rs 110"
            )
        )
        list.add(
            OrderHistoryList(
                0,
                "#0720280002",
                "Sweets",
                "20.08.2020",
                "Cancelled",
                "Rs 110"
            )
        )
        list.add(
            OrderHistoryList(
                0,
                "#0720280002",
                "Sweets",
                "20.08.2020",
                "Delivered",
                "Rs 110"
            )
        )
        list.add(
            OrderHistoryList(
                0,
                "#0720280002",
                "Sweets",
                "20.08.2020",
                "Ongoing",
                "Rs 110"
            )
        )



        return list
    }
}