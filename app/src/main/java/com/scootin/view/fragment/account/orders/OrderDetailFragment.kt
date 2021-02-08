package com.scootin.view.fragment.account.orders


import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.scootin.R
import com.scootin.databinding.FragmentMyOrderTrackBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.request.CancelOrderRequest
import com.scootin.network.response.PaymentDetails
import com.scootin.network.response.inorder.InOrderDetail
import com.scootin.util.UtilUIComponent
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.OrderDetailAdapter
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.account.OrderFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailFragment : BaseFragment(R.layout.fragment_my_order_track) {

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
        binding.btnChangePaymentMode.setOnClickListener {
            findNavController().navigate(OrderDetailFragmentDirections.orderToChangePaymentMode(args.orderId,"NORMAL"))
        }
        setInorderAdapter()
        updateViewModel()
        updateListeners()
        cancelOrder()
    }

    private fun cancelOrder() {

        binding.cancelButton.setOnClickListener {

            val alertDialog = context?.let { it1 -> MaterialAlertDialogBuilder(it1) }

            alertDialog?.setMessage(R.string.dialogMessage)
            alertDialog?.setIcon(android.R.drawable.ic_dialog_alert)


            alertDialog?.setPositiveButton("Yes") { dialogInterface, which ->
                showLoading()
                viewModel.cancelOrder(args.orderId, CancelOrderRequest("NORMAL")).observe(viewLifecycleOwner, {
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

    private fun updateListeners() {
        binding.helpKey.setOnClickListener {
            findNavController().navigate(OrderDetailFragmentDirections.inorderToCustomerSupport())
        }
        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.loadOrder(args.orderId)
            binding.swiperefresh.setRefreshing(false)
        }
    }

    private fun updateViewModel() {
        viewModel.loadOrder(args.orderId)
        viewModel.orderInfo.observe(viewLifecycleOwner, {
            Timber.i("orderId = ${it.status} : ${it.data}")

            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    binding.data = it.data
                    updatePaymentMode(it.data?.orderDetails?.paymentDetails)
                    orderDetailAdapter.submitList(it.data?.orderInventoryDetailsList)

                    updateDate(it.data)

                    updateSelectors(it.data?.orderDetails?.orderStatus)
                    val cancelBtnVisibility = it.data?.orderDetails?.orderStatus == "DISPATCHED" || it.data?.orderDetails?.orderStatus=="COMPLETED" || it.data?.orderDetails?.orderStatus == "CANCEL"

                    binding.cancelButton.updateVisibility(cancelBtnVisibility.not())

                    binding.btnChangePaymentMode.updateVisibility(
                        cancelBtnVisibility&&
                                it.data?.orderDetails?.paymentDetails?.paymentMode=="CASH"&&
                                it.data.orderDetails.orderStatus !="COMPLETED"&&
                                it.data.orderDetails.orderStatus !="CANCEL")
                }
            }
        })
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

    private fun updatePaymentMode(paymentDetails: PaymentDetails?) {
        binding.pay.text = UtilUIComponent.getPaymentStatusText(paymentDetails)
        if(paymentDetails?.paymentStatus=="COMPLETED"){
            binding.pay.setTextColor(Color.parseColor("#3cb043"))
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
                    binding.packedIcon.isSelected = true
                    binding.orderStatusString.text = getString(R.string.order_has_been_packed)
                }
                "DISPATCHED" -> {
                    binding.placeIcon.isSelected = true
                    binding.packedIcon.isSelected = true
                    binding.dispatchedIcon.isSelected = true
                    binding.orderStatusString.text = getString(R.string.order_has_been_dispatched)
                }
                "COMPLETED" -> {
                    binding.placeIcon.isSelected = true
                    binding.packedIcon.isSelected = true
                    binding.dispatchedIcon.isSelected = true
                    binding.deliveredIcon.isSelected = true
                    binding.orderStatusString.text = getString(R.string.order_has_been_completed)
                }
                "CANCEL" -> {
                    binding.orderStatusString.text = getString(R.string.order_has_been_cancelled)
                    binding.placeIcon.isSelected = false
                    binding.packedIcon.isSelected = false
                    binding.dispatchedIcon.isSelected = false
                    binding.deliveredIcon.isSelected = false
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