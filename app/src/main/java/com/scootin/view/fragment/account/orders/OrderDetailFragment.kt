package com.scootin.view.fragment.account.orders

import com.scootin.view.fragment.account.orders.OrderDetailFragmentArgs
import com.scootin.view.fragment.account.orders.OrderDetailFragmentDirections

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
import com.scootin.network.response.inorder.InOrderDetail
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.OrderDetailAdapter
import com.scootin.viewmodel.account.OrderFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailFragment : Fragment(R.layout.fragment_my_order_track) {

    private var binding by autoCleared<FragmentMyOrderTrackBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private val viewModel: OrderFragmentViewModel by viewModels()
    private lateinit var orderDetailAdapter: OrderDetailAdapter
    private val args: OrderDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyOrderTrackBinding.bind(view)
        binding.back.setOnClickListener { findNavController().popBackStack() }
        binding.lifecycleOwner = this
        setInorderAdapter()
        updateViewModel()
        updateListeners()
    }

    private fun updateListeners() {
        binding.helpKey.setOnClickListener {
            findNavController().navigate(OrderDetailFragmentDirections.inorderToCustomerSupport())
        }
        binding.back.setOnClickListener { findNavController().popBackStack() }
    }

    private fun updateViewModel() {
        viewModel.getOrder(args.orderId).observe(viewLifecycleOwner, Observer {
            Timber.i("orderId = ${it.status} : ${it.data}")
            when (it.status) {
                Status.SUCCESS -> {
                    binding.data = it.data
                    orderDetailAdapter.submitList(it.data?.orderInventoryDetailsList)
                    updateSelectors(it.data?.orderDetails?.orderStatus)

                }
            }
        })
    }

    private fun updateSelectors(orderStatus: String?) {
        orderStatus?.let {
            when(it) {
                "PLACED" -> {
                    binding.placeIcon.isSelected = true
                }
                "PACKED" -> {
                    binding.placeIcon.isSelected = true
                    binding.progressId.isSelected=true
                    binding.packedIcon.isSelected = true
                }
                "DISPATCHED" -> {
                    binding.placeIcon.isSelected = true
                    binding.progressId.isSelected=true
                    binding.packedIcon.isSelected = true
                    binding.progressId2.isSelected=true
                    binding.dispatchedIcon.isSelected = true
                }
                "COMPLETED" -> {
                    binding.placeIcon.isSelected = true
                    binding.progressId.isSelected=true
                    binding.packedIcon.isSelected = true
                    binding.progressId2.isSelected=true
                    binding.dispatchedIcon.isSelected = true
                    binding.progressId3.isSelected=true
                    binding.deliveredIcon.isSelected = true
                }
            }
       }

    }
    private fun setInorderAdapter() {
        orderDetailAdapter = OrderDetailAdapter(appExecutors)
        binding.orderList.apply {
            adapter = orderDetailAdapter
        }
    }

}