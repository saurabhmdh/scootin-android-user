package com.scootin.view.fragment.account.orders

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.scootin.R
import com.scootin.databinding.FragmentTrackDirectOrderBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.request.CancelOrderRequest
import com.scootin.network.response.PaymentDetails
import com.scootin.util.Conversions
import com.scootin.util.UtilUIComponent
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.ExtraDataAdapter
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.account.OrderFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DirectOrderDetailFragment : BaseFragment(R.layout.fragment_track_direct_order) {

    private var binding by autoCleared<FragmentTrackDirectOrderBinding>()

    private val viewModel: OrderFragmentViewModel by viewModels()
    private var itemsAdapter by autoCleared<ExtraDataAdapter>()
    private val args: DirectOrderDetailFragmentArgs by navArgs()

    private var express: Boolean = false

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrackDirectOrderBinding.bind(view)
        binding.back.setOnClickListener { findNavController().popBackStack() }
        updateViewModel()
        updateListeners()
        initUI()
        cancelOrder()
    }

    private fun updateViewModel() {
        viewModel.loadOrder(args.orderId)

        viewModel.directOrder.observe(viewLifecycleOwner, {
            Timber.i("orderId = ${it.status} : ${it.data}")
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    binding.data = it.data

                    Timber.i("Check for order status and payment status ${it.data?.orderStatus}, ${it.data?.paymentDetails?.paymentStatus}")
                    express = it.data?.expressDelivery == true

                    val canPay = (it.data?.orderStatus == "PACKED" || it.data?.orderStatus == "DISPATCHED") && it.data.paymentDetails.paymentStatus == "CREATED"
                    updatePaymentMode(it.data?.paymentDetails, canPay)


                    updateSelectors(it.data?.orderStatus)

                    if (it.data?.extraData.isNullOrEmpty().not()) {
                        val extra = Conversions.convertExtraData(it.data?.extraData)
                        Timber.i("Extra $extra")
                        itemsAdapter.submitList(extra)
                    }
                    if (it.data?.media == null) {
                        binding.orderList.visibility = View.GONE
                    }

                    val cancelBtnVisibility = it.data?.orderStatus == "DISPATCHED" || it.data?.orderStatus=="COMPLETED" || it.data?.orderStatus == "CANCEL"
                    binding.cancelButton.updateVisibility(cancelBtnVisibility.not())

                    //Display payment mode.. If payment is not done then we can ask user to pay now..


                }
            }
        })
    }

    private fun updatePaymentMode(paymentDetails: PaymentDetails?, canPay: Boolean) {
        binding.payNow.updateVisibility(canPay)
        binding.payOnDeliveryHeader.updateVisibility(canPay.not())
        binding.pay.updateVisibility(canPay.not())
        binding.pay.text = UtilUIComponent.getPaymentStatusText(paymentDetails)
        if(paymentDetails?.paymentStatus=="COMPLETED"){
            binding.pay.setTextColor(Color.parseColor("#3cb043"))
        }
    }


    private fun updateListeners() {

        binding.helpKey.setOnClickListener {
            findNavController().navigate(DirectOrderDetailFragmentDirections.orderToCustomerSupport())
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.payNow.setOnClickListener {
            findNavController().navigate(DirectOrderDetailFragmentDirections.directOrderToPayment(args.orderId, "DIRECT", express))
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.loadOrder(args.orderId)
            binding.swiperefresh.setRefreshing(false)
        }
    }

    private fun updateSelectors(orderStatus: String?) {
        orderStatus?.let {
            when(it) {
                "PLACED" -> {
                    binding.placeIcon.isSelected = true
                    binding.orderStatusString.text = getString(R.string.order_has_been_placed)
                }
                "PACKED" -> {
                    binding.placeIcon.isSelected = true
                    binding.progressId.isSelected=true
                    binding.packedIcon.isSelected = true
                    binding.orderStatusString.text = getString(R.string.order_has_been_packed)
                }
                "DISPATCHED" -> {
                    binding.placeIcon.isSelected = true
                    binding.progressId.isSelected=true
                    binding.packedIcon.isSelected = true
                    binding.progressId2.isSelected=true
                    binding.dispatchedIcon.isSelected = true
                    binding.orderStatusString.text = getString(R.string.order_has_been_dispatched)
                }
                "COMPLETED" -> {
                    binding.placeIcon.isSelected = true
                    binding.progressId.isSelected=true
                    binding.packedIcon.isSelected = true
                    binding.progressId2.isSelected=true
                    binding.dispatchedIcon.isSelected = true
                    binding.progressId3.isSelected=true
                    binding.deliveredIcon.isSelected = true
                    binding.orderStatusString.text = getString(R.string.order_has_been_completed)
                }
                "CANCEL" -> {
                    binding.orderStatusString.text = getString(R.string.order_has_been_cancelled)
                    binding.placeIcon.isSelected = false
                    binding.progressId.isSelected = false
                    binding.packedIcon.isSelected = false
                    binding.progressId2.isSelected = false
                    binding.dispatchedIcon.isSelected = false
                    binding.progressId3.isSelected = false
                    binding.deliveredIcon.isSelected = false
                }
            }
        }

    }

    private fun cancelOrder() {

        binding.cancelButton.setOnClickListener {

            val alertDialog = context?.let { it1 -> MaterialAlertDialogBuilder(it1) }

            alertDialog?.setMessage(R.string.dialogMessage)
            alertDialog?.setIcon(android.R.drawable.ic_dialog_alert)


            alertDialog?.setPositiveButton("Yes") { dialogInterface, which ->
                showLoading()
                viewModel.cancelOrder(args.orderId, CancelOrderRequest("DIRECT")).observe(viewLifecycleOwner, {
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
                Toast.makeText(context,"Please collect cash from customer", Toast.LENGTH_LONG).show()
            }



            alertDialog?.setCancelable(false)

            alertDialog?.show()
//           

        }
    }

    private fun initUI() {
        itemsAdapter = ExtraDataAdapter(appExecutors)
        binding.recycler.adapter=itemsAdapter
    }
}