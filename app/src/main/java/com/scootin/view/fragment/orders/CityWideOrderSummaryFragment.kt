package com.scootin.view.fragment.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentCitywideOrderSummaryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class CityWideOrderSummaryFragment : Fragment(R.layout.fragment_citywide_order_summary) {
    private var binding by autoCleared<FragmentCitywideOrderSummaryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors

    private val viewModel: PaymentViewModel by viewModels()

    private val args: CityWideOrderSummaryFragmentArgs by navArgs()

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCitywideOrderSummaryBinding.bind(view)

        setupListener()
    }

    private fun setupListener() {
        viewModel.loadOrder(orderId)
        viewModel.citywideOrderInfo.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.data = it.data
                    Timber.i("data working ${it.data}")

                    if (it.data?.media == null) {
                        binding.media.visibility = View.GONE
                    }
                }
                Status.ERROR -> {
                    //Show error and move back
                }
            }
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack(R.id.titleScreen, false)
        }
        binding.helpBtn.setOnClickListener {
            findNavController().navigate(CityWideOrderSummaryFragmentDirections.orderToCustomerSupport())
        }
    }



    //If same shop then once

}