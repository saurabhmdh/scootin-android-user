package com.scootin.view.fragment.account.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentTrackCitywideOrderBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.request.CancelOrderRequest
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.account.OrderFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CityWideOrderDetailFragment : BaseFragment(R.layout.fragment_track_citywide_order) {

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
        cancelOrder()
    }

    private fun updateViewModel() {
        viewModel.loadOrder(args.orderId)

        viewModel.cityWideOrder.observe(viewLifecycleOwner, Observer {
            Timber.i("orderId = ${it.status} : ${it.data}")
            when (it.status) {
                Status.SUCCESS -> {
                    binding.data = it.data

                    val cancelBtnVisibility = it.data?.orderStatus == "DISPATCHED" || it.data?.orderStatus=="COMPLETED" || it.data?.orderStatus == "CANCEL"
                    binding.cancelButton.updateVisibility(cancelBtnVisibility.not())
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

    private fun cancelOrder() {

        binding.cancelButton.setOnClickListener {
            showLoading()
            viewModel.cancelOrder(args.orderId, CancelOrderRequest("CITYWIDE")).observe(viewLifecycleOwner, {
                Timber.i("orderId = ${it.status} : ${it.data}")
                when (it.status) {
                    Status.SUCCESS -> {
                        viewModel.loadOrder(args.orderId)
                    }
                }
            })
        }
    }

}