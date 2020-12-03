package com.scootin.view.fragment.account.orders


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
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.OrderDetailAdapter
import com.scootin.view.fragment.cart.CardPaymentPageFragmentDirections
import com.scootin.viewmodel.account.OrderFragmentViewModel
import com.scootin.viewmodel.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_orders.view.*
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailFragment : Fragment(R.layout.fragment_my_order_track) {

    private var binding by autoCleared<FragmentMyOrderTrackBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private val viewModel: OrderFragmentViewModel by viewModels()
    private val viewModel2: PaymentViewModel by viewModels()
    var orderId: Long = -1
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
        if(binding.pay.text=="Pay Now"){

        }
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
                    orderId = it.data?.orderDetails?.id ?: -1
                    orderDetailAdapter.submitList(it.data?.orderInventoryDetailsList)
                    updateSelectors(it.data?.orderDetails?.orderStatus)
                    if(it.data?.orderDetails?.paymentDetails?.paymentStatus=="COMPLETED"){
                        binding.payOnDeliveryHeader.setText("Payment Completed")
                    }
                    else {
                        binding.payOnDeliveryHeader.setText("Payment Pending")
                        binding.pay.setText("Pay Now")
                        val total = it.data?.orderDetails?.paymentDetails?.totalAmount.orDefault(0.0) * 100

                         val start=   it.data?.orderDetails?.paymentDetails?.orderReference.orEmpty()
                        binding.pay.setOnClickListener {
                            startPayment(start,total)
                        }

                    }
                }
            }
        })
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
            }
       }

    }
    private fun setInorderAdapter() {
        orderDetailAdapter = OrderDetailAdapter(appExecutors)
        binding.orderList.apply {
            adapter = orderDetailAdapter
        }
    }
    private fun startPayment(orderReferenceId: String, price: Double) {
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", "Scootin Inc")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://image-res.s3.ap-south-1.amazonaws.com/scootin-logo.png")
            options.put("theme.color", "#E90000")
            options.put("currency", "INR")
            options.put("amount", price)
            options.put("order_id", orderReferenceId)
            val prefill = JSONObject()
            prefill.put("email","support@scootin.co.in")
            prefill.put("contact", AppHeaders.userMobileNumber)

            options.put("prefill", prefill)
            co.open(activity, options)

        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    fun onPaymentSuccess(razorpayPaymentId: String?){
        Timber.i("onPaymentSuccess = ${razorpayPaymentId} $orderId")
        viewModel2.verifyPayment(VerifyAmountRequest(razorpayPaymentId)).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    //Need some direction to move

                }
                Status.ERROR -> {}
            }
        }
    }


}