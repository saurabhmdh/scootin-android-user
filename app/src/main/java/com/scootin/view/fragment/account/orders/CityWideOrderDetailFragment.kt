package com.scootin.view.fragment.account.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentTrackCitywideOrderBinding
import com.scootin.databinding.FragmentTrackDirectOrderBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.util.Conversions
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.ExtraDataAdapter
import com.scootin.view.fragment.account.orders.DirectOrderDetailFragmentDirections.orderToCustomerSupport
import com.scootin.viewmodel.account.OrderFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class CityWideOrderDetailFragment : Fragment(R.layout.fragment_track_citywide_order) {

    private var binding by autoCleared<FragmentTrackCitywideOrderBinding>()
    private val viewModel: OrderFragmentViewModel by viewModels()
    private val args: DirectOrderDetailFragmentArgs by navArgs()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrackCitywideOrderBinding.bind(view)
        binding.back.setOnClickListener { findNavController().popBackStack() }
        updateViewModel()
        updateListeners()

    }

    private fun updateViewModel() {
        viewModel.getCityWideOrder(args.orderId).observe(viewLifecycleOwner, Observer {
            Timber.i("orderId = ${it.status} : ${it.data}")
            when (it.status) {
                Status.SUCCESS -> {
                    binding.data = it.data
                }
            }
        })
    }



    private fun updateListeners() {

        binding.helpKey.setOnClickListener {
            findNavController().navigate(CityWideOrderDetailFragmentDirections.citywideToCustomerSupport())
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }

    }



}