package com.scootin.view.fragment.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentOrderHistoryBinding
import com.scootin.databinding.FragmentOrderSummaryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.response.orders.OrderHistoryList
import com.scootin.network.response.orders.OrderedItemsList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.OrderHistoryAdapter
import com.scootin.view.adapter.order.OrderSummaryAdapter
import com.scootin.view.fragment.cart.CardPaymentPageFragmentArgs
import com.scootin.viewmodel.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class OrderSummaryFragment :Fragment(R.layout.fragment_order_summary) {
    private var binding by autoCleared<FragmentOrderSummaryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var orderSummaryAdapter: OrderSummaryAdapter

    private val viewModel: PaymentViewModel by viewModels()

    private val args: OrderSummaryFragmentArgs by navArgs()

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderSummaryBinding.bind(view)
        setAdaper()
        setupListener()
    }

    private fun setupListener() {
        viewModel.loadOrder(orderId)
        viewModel.orderInfo.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.data = it.data
                    Timber.i("data working ${it.data}")

                    orderSummaryAdapter.submitList(it.data?.orderInventoryDetailsList)
                }
                Status.ERROR -> {
                    //Show error and move back
                }
            }
        }
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

}