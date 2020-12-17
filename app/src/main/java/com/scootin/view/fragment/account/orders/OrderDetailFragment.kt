package com.scootin.view.fragment.account.orders


import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.razorpay.Checkout
import com.scootin.R
import com.scootin.databinding.FragmentMyOrderTrackBinding
import com.scootin.extensions.orDefault
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.CancelOrderRequest
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.network.response.PaymentDetails
import com.scootin.util.UtilUIComponent
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.OrderDetailAdapter
import com.scootin.view.fragment.BaseFragment
import com.scootin.view.fragment.cart.CardPaymentPageFragmentDirections
import com.scootin.viewmodel.account.OrderFragmentViewModel
import com.scootin.viewmodel.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_orders.view.*
import org.json.JSONObject
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
        setInorderAdapter()
        updateViewModel()
        updateListeners()
        cancelOrder()
    }

    private fun cancelOrder() {

        binding.cancelButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setTitle(R.string.dialogTitle)
            //set message for alert dialog
            builder.setMessage(R.string.dialogMessage)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
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
            //performing cancel action
            builder.setNeutralButton("No") { dialogInterface, which ->

            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

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
                    updateSelectors(it.data?.orderDetails?.orderStatus)
                    val cancelBtnVisibility = it.data?.orderDetails?.orderStatus == "DISPATCHED" || it.data?.orderDetails?.orderStatus=="COMPLETED" || it.data?.orderDetails?.orderStatus == "CANCEL"
                    binding.cancelButton.updateVisibility(cancelBtnVisibility.not())

                }
            }
        })
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
    private fun setInorderAdapter() {
        orderDetailAdapter = OrderDetailAdapter(appExecutors)
        binding.orderList.apply {
            adapter = orderDetailAdapter
        }
    }
}