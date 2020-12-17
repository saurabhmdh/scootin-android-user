package com.scootin.view.fragment.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentOrderSummaryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.inorder.OrderInventoryDetails
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.OrderSummaryAdapter
import com.scootin.viewmodel.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.StringBuilder
import javax.inject.Inject

@AndroidEntryPoint
class OrderSummaryFragment :Fragment(R.layout.fragment_order_summary) {
    private var binding by autoCleared<FragmentOrderSummaryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var orderSummaryAdapter: OrderSummaryAdapter

    private val viewModel: PaymentViewModel by viewModels()

    private val args: OrderSummaryFragmentArgs by navArgs()

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderSummaryBinding.bind(view)
        setAdaper()
        setupListener()
    }

    private fun setupListener() {
        viewModel.loadOrder(orderId)
        viewModel.orderInfo.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.data = it.data
                    Timber.i("data working ${it.data}")
                    binding.txtPickupLocation.text = getAllAddress(it.data?.orderInventoryDetailsList)
                    orderSummaryAdapter.submitList(it.data?.orderInventoryDetailsList)
                    if(it.data?.orderDetails?.paymentDetails?.paymentStatus=="COMPLETED"){
                        binding.paymentStatus.setText("Paid Online")
                    }
                    else{
                        binding.paymentStatus.setText("Pay on Delivery")
                    }
                }
                Status.ERROR -> {
                    //Show error and move back
                }
            }
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack(R.id.cart, false)
        }
        binding.helpBtn.setOnClickListener {
            findNavController().navigate(OrderSummaryFragmentDirections.orderToCustomerSupport())
        }
    }

    private fun getAllAddress(orderInventoryDetailsList: List<OrderInventoryDetails>?): String {
        val uniqueAddress = mutableSetOf<Long>()

        val sb = StringBuffer()
        orderInventoryDetailsList?.forEach {
            if(uniqueAddress.contains(it.inventoryDetails.shopManagement.id).not()) {
                sb.append(it.inventoryDetails.shopManagement.name).append(", ")
                sb.append(getSingleAddress(it.inventoryDetails.shopManagement.address))
                sb.append("\n")
                uniqueAddress.add(it.inventoryDetails.shopManagement.id)
            }
        }
        return sb.toString()
    }

    private fun setAdaper() {
        orderSummaryAdapter =
            OrderSummaryAdapter(
                appExecutors,
                object : OrderSummaryAdapter.ImageAdapterClickLister {
                    override fun onViewDetailsSelected(view: View) {
                    }
                })

        binding.listOfItemsRecycler.apply {
            adapter = orderSummaryAdapter
        }
    }

    //If same shop then once
    private fun getSingleAddress(address: AddressDetails?): String {
        if(address == null) return ""
        val sb = StringBuilder().append(address.addressLine1).append(", ")
            .append(address.addressLine2).append(", ").append(address.city).append(", ")
            .append(address.pincode)
        return sb.toString()
    }

}