package com.scootin.view.fragment.account.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.scootin.R
import com.scootin.databinding.FragmentTrackCitywideOrderBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.request.CancelOrderRequest
import com.scootin.network.response.citywide.CityWideOrderResponse
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
        binding.btnChangePaymentMode.setOnClickListener {
            findNavController().navigate(CityWideOrderDetailFragmentDirections.cityWideOrderToChangePayment(args.orderId,"CITYWIDE"))
        }
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

                    Timber.i("Saurabh ${it.data?.paymentDetails}")

                    val canPay = ((it.data?.orderStatus == "PACKED" || it.data?.orderStatus == "DISPATCHED") && it.data.paymentDetails.paymentStatus == "CREATED")
                    updatePaymentMode(canPay)

                    val onDelivery = ((it.data?.orderStatus == "PACKED" || it.data?.orderStatus == "DISPATCHED") && it.data.paymentDetails.paymentStatus == "COMPLETED")
                    updatePaymentText(onDelivery)

                    if (it.data?.orderStatus == "COMPLETED") {
                        binding.orderStatusString.text = getString(R.string.order_has_been_completed)
                    }
                    updateDate(it.data)
                    val cancelBtnVisibility = it.data?.orderStatus == "DISPATCHED" || it.data?.orderStatus=="COMPLETED" || it.data?.orderStatus == "CANCEL"
                    binding.cancelButton.updateVisibility(cancelBtnVisibility.not())
                    binding.btnChangePaymentMode.updateVisibility(cancelBtnVisibility&&it.data?.paymentDetails?.paymentMode=="CASH"&&it.data?.orderStatus!="COMPLETED")

                }
            }
        })
    }

    private fun updateDate(data: CityWideOrderResponse?) {
        data?.let {
            val orderText = if (it.deliveryDetails?.deliveredDateTime == null) {
                "Order Date: "
            } else {
                "Delivery Date: "
            }
            val latestDate = it.deliveryDetails?.deliveredDateTime ?: it.orderDate
            binding.orderDateTime.text = orderText + latestDate
        }
    }

    private fun updatePaymentText(onDelivery: Boolean) {
        if (onDelivery) {
            binding.orderStatusString.text = getString(R.string.city_wide_order_pay)
        }
    }


    private fun updateListeners() {

        binding.helpKey.setOnClickListener {
            findNavController().navigate(CityWideOrderDetailFragmentDirections.citywideToCustomerSupport())
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.payNow.setOnClickListener {
            findNavController().navigate(CityWideOrderDetailFragmentDirections.cityWideOrderToPayment(args.orderId, "CITYWIDE", false))
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.loadOrder(args.orderId)
            binding.swiperefresh.setRefreshing(false)
        }
    }

    private fun cancelOrder() {

        binding.cancelButton.setOnClickListener {
            val alertDialog = context?.let { it1 -> MaterialAlertDialogBuilder(it1) }

            alertDialog?.setMessage(R.string.dialogMessage)
            alertDialog?.setIcon(android.R.drawable.ic_dialog_alert)


            alertDialog?.setPositiveButton("Yes") { dialogInterface, which ->
                showLoading()
                viewModel.cancelOrder(args.orderId, CancelOrderRequest("CITYWIDE")).observe(viewLifecycleOwner, {
                    Timber.i("orderId = ${it.status} : ${it.data}")
                    when (it.status) {
                        Status.SUCCESS -> {
                            viewModel.loadOrder(args.orderId)
                            dismissLoading()
                            findNavController().navigate(OrderDetailFragmentDirections.orderToCancelOrder())

                        }
                    }
                })
            }

            alertDialog?.setNegativeButton("No") { dialogInterface, which ->
            }



            alertDialog?.setCancelable(false)

            alertDialog?.show()

        }
    }

    private fun updatePaymentMode(canPay: Boolean) {
        binding.payNow.updateVisibility(canPay)
        if (canPay) {
            binding.orderStatusString.text = getString(R.string.order_not_pay)
        }
    }

}