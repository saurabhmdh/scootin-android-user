package com.scootin.view.fragment.orders

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentOrderSummaryBinding
import com.scootin.extensions.orDefault
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.inorder.InOrderDetail
import com.scootin.network.response.inorder.MultipleOrdersDetails
import com.scootin.network.response.inorder.OrderInventoryDetails
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.OrderSummaryAdapter
import com.scootin.view.fragment.BaseFragment
import com.scootin.view.vo.MultiOrderVo
import com.scootin.viewmodel.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.StringBuilder
import javax.inject.Inject

@AndroidEntryPoint
class OrderSummaryFragment : BaseFragment (R.layout.fragment_order_summary) {
    private var binding by autoCleared<FragmentOrderSummaryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var orderSummaryAdapter: OrderSummaryAdapter

    private val viewModel: PaymentViewModel by viewModels()

    private val args: OrderSummaryFragmentArgs by navArgs()

    private val orderId by lazy {
        args.orderId.asList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderSummaryBinding.bind(view)
        setAdaper()
        setupListener()
    }

    private fun setupListener() {

        viewModel.loadMultipleOrders(orderId)

        showLoading()
        viewModel.multipleOrderInfo.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    Timber.i("data working ${it.data}")
                    if (it.data == null) {
                       Toast.makeText(requireContext(), "There is server error", Toast.LENGTH_SHORT).show()
                       return@observe
                    }

                    binding.data = convertToVo(it.data)

                    binding.storeName.text = getAllAddress(it.data.orderInventoryDetailsList)
                    orderSummaryAdapter.submitList(it.data.orderInventoryDetailsList)

                    if (it.data.orderDetails.first().paymentDetails.paymentStatus=="COMPLETED"){
                        binding.paymentMode.setText("Paid Online")
                    } else{
                        binding.paymentMode.setText("Pay on Delivery")
                    }
                }
                Status.ERROR -> {
                    viewModel.loadMultipleOrders(orderId)
                }
                Status.LOADING -> {

                }
            }
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack(R.id.cart, false)
        }
//        binding.he.setOnClickListener {
//            findNavController().navigate(OrderSummaryFragmentDirections.orderToCustomerSupport())
//        }
    }

    private fun updateDate(data: InOrderDetail?) {
        data?.let {
            val orderText = if (it.orderDetails.deliveryDetails?.deliveredDateTime == null) {
                "Order Date: "
            } else {
                "Delivery Date: "
            }
            val latestDate = it.orderDetails.deliveryDetails?.deliveredDateTime ?: it.orderDetails.orderDate
            binding.orderDateTime.text = orderText + latestDate
        }

    }

    private fun convertToVo(data: MultipleOrdersDetails): MultiOrderVo {
        val multiOrders = data.orderDetails.joinToString { it.id.toString() }
        val deliveryAddress = data.orderDetails.first().addressDetails
        val amount = data.orderDetails.sumByDouble { it.paymentDetails.amount.orDefault(0.0) }
        val deliveryFreeAmount = data.orderDetails.sumByDouble { it.paymentDetails.deliveryFreeAmount.orDefault(0.0) }
        val totalGSTAmount = data.orderDetails.sumByDouble { it.paymentDetails.totalGSTAmount.orDefault(0.0) }
        val totalSaving = data.orderDetails.sumByDouble { it.paymentDetails.totalSaving.orDefault(0.0) }
        val totalAmount = data.orderDetails.sumByDouble { it.paymentDetails.totalAmount.orDefault(0.0) }
        val orderDate=data.orderDetails.joinToString { it.orderDate }

        return MultiOrderVo(multiOrders, deliveryAddress, amount, deliveryFreeAmount, totalGSTAmount, totalSaving, totalAmount,orderDate)
    }


    private fun getAllAddress(orderInventoryDetailsList: List<OrderInventoryDetails>?): String {
        val uniqueAddress = mutableSetOf<Long>()

        val sb = StringBuffer()
        orderInventoryDetailsList?.forEach {
            if(uniqueAddress.contains(it.inventoryDetails.shopManagement.id).not()) {
                sb.append(it.inventoryDetails.shopManagement.name)
                    //.append(", ")
//                sb.append(getSingleAddress(it.inventoryDetails.shopManagement.address))
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