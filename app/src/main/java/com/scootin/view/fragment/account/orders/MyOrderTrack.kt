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
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.MyOrderCartViewModel
import com.scootin.viewmodel.account.OrderFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyOrderTrack : Fragment(R.layout.fragment_my_order_track) {

    private var binding by autoCleared<FragmentMyOrderTrackBinding>()
//    private val viewModel: MyOrderCartViewModel by viewModels()

    private val viewModel: OrderFragmentViewModel by viewModels()

    private val args: MyOrderTrackArgs by navArgs()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyOrderTrackBinding.bind(view)
        updateView()
        updateViewModel()
        updateListeners()
    }

    private fun updateViewModel() {
        viewModel.getDirectOrder(args.orderId).observe(viewLifecycleOwner, Observer {
            Timber.i("orderId = ${it.status} : ${it.data}")
        })
    }

    private fun updateView() {
        val orderId = args.orderId
       /* binding.orderId.text = "Order ID: ${order.id}"
        binding.orderDateTime.text = "Placed on ${order.orderDate.epochSecond}"
        binding.orderDateTimeHeader.text = "Placed on ${order.orderDate.epochSecond}"
        binding.totalAmount.text = "Amount Rs. ${order.totalAmount}"
        binding.itemCount.text = "30 items"*/
    }

    private fun updateListeners() {
        binding.back.setOnClickListener { findNavController().popBackStack() }
    }

}