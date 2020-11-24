package com.scootin.view.fragment.orders

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderPlacedFragment : Fragment(R.layout.fragment_order_placed) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            findNavController().popBackStack()
        }, 1500)
    }
}