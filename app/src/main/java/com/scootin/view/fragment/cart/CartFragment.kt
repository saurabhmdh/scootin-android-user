package com.scootin.view.fragment.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentCitywideDeliveryBinding
import com.scootin.databinding.FragmentPaymentPageBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.delivery.clothing.ClothingCategoryFragmentDirections
import com.scootin.view.fragment.dialogs.CitywideCategoryDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment: Fragment(R.layout.fragment_payment_page) {
    private var binding by autoCleared<FragmentPaymentPageBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaymentPageBinding.bind(view)
        binding.payByCardsTab.setOnClickListener {
//            findNavController().navigate(CartFragmentDirections.actionCartFragmentToCardPayment())
        }

    }
}