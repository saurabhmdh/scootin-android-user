package com.scootin.view.fragment.orders

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderConfirmedFragment : Fragment(R.layout.fragment_order_confirmed) {

    private val args: OrderConfirmedFragmentArgs by navArgs()

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            findNavController().navigate(OrderConfirmedFragmentDirections.actionCartPaymentFragmentToOrderSummary(orderId))
        }, 3000)
    }
}