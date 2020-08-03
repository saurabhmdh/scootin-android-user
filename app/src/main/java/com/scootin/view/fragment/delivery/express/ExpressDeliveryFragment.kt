package com.scootin.view.fragment.delivery.express

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentExpressDeliveryBinding
import com.scootin.databinding.FragmentHomeBinding
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.dialogs.CategoryDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpressDeliveryFragment: Fragment(R.layout.fragment_express_delivery) {
    private var binding by autoCleared<FragmentExpressDeliveryBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpressDeliveryBinding.bind(view)

        val lottieDialogFragment = CategoryDialogFragment()
        lottieDialogFragment.show(childFragmentManager, "")

    }
}