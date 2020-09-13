package com.scootin.view.fragment.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentOrderHistoryBinding
import com.scootin.databinding.FragmentOrderSummaryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.orders.OrderHistoryList
import com.scootin.network.response.orders.OrderedItemsList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.OrderHistoryAdapter
import com.scootin.view.adapter.order.OrderSummaryAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class OrderSummaryFragment :Fragment(R.layout.fragment_order_summary) {
    private var binding by autoCleared<FragmentOrderSummaryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var orderSummaryAdapter: OrderSummaryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderSummaryBinding.bind(view)
        setAdaper()
        orderSummaryAdapter.submitList(setList())

    }
    private fun setAdaper() {
        orderSummaryAdapter =
            OrderSummaryAdapter(
                appExecutors,
                object : OrderSummaryAdapter.ImageAdapterClickLister {
                    override fun onViewDetailsSelected(view: View) {

                    }


                })

        binding.listOfItemsRecycler.apply {
            adapter = orderSummaryAdapter
        }
    }
    private fun setList(): ArrayList<OrderedItemsList> {
        val list = ArrayList<OrderedItemsList>()
        list.add(
            OrderedItemsList(
                "Item 01",
            "280",
                0
            )
        )
        list.add(
            OrderedItemsList(
                "Item 02",
                "80",
                0
            )
        )
        list.add(
            OrderedItemsList(
                "Item 03",
                "20",
                0
            )
        )
        list.add(
            OrderedItemsList(
                "Item 04",
                "50",
                0
            )
        )

        return list
    }


}