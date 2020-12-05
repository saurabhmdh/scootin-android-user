package com.scootin.view.fragment.orders

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CitywideOrderConfirmedFragment : Fragment(R.layout.fragment_order_confirmed) {

    private val args: OrderConfirmedFragmentArgs by navArgs()

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.i("We received this order id $orderId")
        Handler().postDelayed({
            findNavController().navigate(CitywideOrderConfirmedFragmentDirections.actionConfirmationFragmentToOrderSummary(orderId))
        }, 3000)
    }
}