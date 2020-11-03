package com.scootin.view.fragment.account.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentMyOrderTrackBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.response.orderdetail.OrderDetail
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.OrderFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DirectOrderDetailFragment : Fragment(R.layout.fragment_my_order_track) {

    private var binding by autoCleared<FragmentMyOrderTrackBinding>()
//    private val viewModel: MyOrderCartViewModel by viewModels()

    private val viewModel: OrderFragmentViewModel by viewModels()

    private val args: DirectOrderDetailFragmentArgs by navArgs()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyOrderTrackBinding.bind(view)
        updateViewModel()
        updateListeners()
    }

    private fun updateViewModel() {
        viewModel.getDirectOrder(args.orderId).observe(viewLifecycleOwner, Observer {
            Timber.i("orderId = ${it.status} : ${it.data}")
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    updateView(data)
                }
            }
        })
    }

    private fun updateView(data: OrderDetail?) {
        data?.let {
            it.apply {
                binding.orderId.text = "Order ID: ${id}"
                binding.orderDateTime.text = "Placed on ${orderDate}"
                binding.orderDateTimeHeader.text = "Placed on ${orderDate}"
                binding.totalAmount.text = "Amount Rs. ${paymentDetails.deliveryFreeAmount}"
                binding.itemCount.text = "30 items"
            }
        }
    }

    private fun updateListeners() {

        binding.helpKey.setOnClickListener {
            findNavController().navigate(DirectOrderDetailFragmentDirections.orderToCustomerSupport())
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }

    }

}