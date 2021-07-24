package com.scootin.view.fragment.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentDirectOrderSummaryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.orderdetail.ShopManagement
import com.scootin.util.Conversions
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.DirectOrderSummaryAdapter
import com.scootin.viewmodel.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.StringBuilder
import javax.inject.Inject
@AndroidEntryPoint
class DirectOrderSummaryFragment: Fragment(R.layout.fragment_direct_order_summary) {
    private var binding by autoCleared<FragmentDirectOrderSummaryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var orderSummaryAdapter: DirectOrderSummaryAdapter

    private val viewModel: PaymentViewModel by viewModels()

    private val args: DirectOrderSummaryFragmentArgs by navArgs()

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDirectOrderSummaryBinding.bind(view)
        setAdaper()
        setupListener()
    }

    private fun getAllAddress(shopManagement: ShopManagement): String {
        val uniqueAddress = mutableSetOf<Long>()

        val sb = StringBuffer()


                sb.append(shopManagement.name).append(", ")
                sb.append(getSingleAddress(shopManagement.address))
                sb.append("\n")
                uniqueAddress.add(shopManagement.id.toLong())


        return sb.toString()
    }

    private fun setupListener() {
        viewModel.loadOrder(orderId)
        viewModel.directOrderInfo.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.data = it.data
                    Timber.i("data working ${it.data}")
                    if (it.data?.extraData.isNullOrEmpty().not()) {
                        val extra = Conversions.convertExtraData(it.data?.extraData)
                        Timber.i("Extra $extra")
                        orderSummaryAdapter.submitList(extra)
                    }

                    if (it.data?.media == null) {
                        binding.media.visibility = View.GONE
                    }
                    binding.storeName.text= it.data?.shopManagement?.let { it1 ->
                        getAllAddress(
                            it1
                        )
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
//
//        binding.helpBtn.setOnClickListener {
//            findNavController().navigate(OrderSummaryFragmentDirections.orderToCustomerSupport())
//        }
    }

    private fun setAdaper() {
        orderSummaryAdapter =
            DirectOrderSummaryAdapter(
                appExecutors)

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