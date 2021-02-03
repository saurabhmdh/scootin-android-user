package com.scootin.view.fragment.orders

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderPlacedFragment : Fragment(R.layout.fragment_order_placed) {

    private val args: OrderPlacedFragmentArgs by navArgs()

    private val orderId by lazy {
        args.orderId.asList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            findNavController().navigate(OrderPlacedFragmentDirections.actionCartPaymentFragmentToOrderSummary(orderId.toLongArray()))
        }, 3000)
    }
}