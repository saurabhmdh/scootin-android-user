package com.scootin.view.fragment.cart

import android.content.Intent
import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.navigation.fragment.navArgs
import com.razorpay.Checkout
import com.scootin.R
import com.scootin.databinding.FragmentPaymenttStatusBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.OrderRequest
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.network.response.placeOrder.PlaceOrderResponse
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.BaseFragment

import com.scootin.viewmodel.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CardPaymentPageFragment : BaseFragment(R.layout.fragment_paymentt_status) {
    private var binding by autoCleared<FragmentPaymenttStatusBinding>()
    private val viewModel: PaymentViewModel by viewModels()

    private val args: CardPaymentPageFragmentArgs by navArgs()

    @Inject
    lateinit var appExecutors: AppExecutors

    //Order ID = 20655
    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaymenttStatusBinding.bind(view)
        Checkout.preload(context)
        setListener()
    }

    private fun setListener() {
        binding.confirmButton.setOnClickListener {

            val mode = when(binding.radioGroup.getCheckedRadioButtonPosition()) {
                0 -> {"ONLINE"}
                1 -> {"CASH"}
                else -> {""}
            }
            showLoading()
            viewModel.userConfirmOrder(orderId.toString(), AppHeaders.userID, OrderRequest(mode)).observe(viewLifecycleOwner) {
                when(it.status) {
                    Status.SUCCESS -> {
                        Timber.i(" data ${it.data}")
                        dismissLoading()
                        startPayment(it.data?.paymentDetails?.orderReference.orEmpty())
                    }
                    Status.ERROR -> {
                        dismissLoading()
                    }
                    Status.LOADING -> {}
                }
            }
        }

        Timber.i("Order ID = " + args.orderId)

//        viewModel.placeOrderLiveData.observe(viewLifecycleOwner, Observer {
//            Timber.i("placeOrder = ${it}")
//            // call payment UI
//
//            val data = it.body()
//            callPaymentUiFunction(data)
//        })

//        viewModel.orderRequestLiveData.observe(viewLifecycleOwner, Observer {
//            Timber.i("orderRequest = ${it}")
//            val response = it.body()
//            orderId = "${response?.id}"
//            // TODO launch payment screen if paymentMode is online
//            if (paymentMode == "ONLINE") {
//                // launch payment screen
//                startPayment(response?.paymentDetails?.orderReference.orEmpty())
//            } else {
//                // launch success screen
//            }
//        })


        binding.applyPromoButton.setOnClickListener {
            viewModel.promCodeRequest(orderId.toString(), binding.couponEdittext.text.toString()).observe(viewLifecycleOwner) {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Coupon has been applied.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Invalid Coupon code", Toast.LENGTH_SHORT).show()
                }
            }
        }


        viewModel.verifyPaymentRequestLiveData.observe(viewLifecycleOwner, {
            Timber.i("verifyPaymentRequestLiveData = $it")
        })
    }

    private fun callPaymentUiFunction(response: PlaceOrderResponse?) {
        //Lets discuss this later
        //orderId = response?.id.toString()
    }

    private fun startPayment(orderReferenceId: String) {
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", "Scootin Inc")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://image-res.s3.ap-south-1.amazonaws.com/scootin-logo.png")
            options.put("theme.color", "#E90000")
            options.put("currency", "INR")
            options.put("order_id", orderReferenceId)
            val prefill = JSONObject()
           prefill.put("email","support@scootin.co.in")
            prefill.put("contact", AppHeaders.userMobileNumber)

            options.put("prefill", prefill)
            co.open(activity, options)

            //Razorpay will return 3 values.. Which we need to check
            //capture-payment
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.i("data := ${data}")
    }

    fun onPaymentSuccess(razorpayPaymentId: String?){
        Timber.i("onPaymentSuccess = ${razorpayPaymentId}")
        viewModel.verifyPaymentRequest(VerifyAmountRequest(razorpayPaymentId))
    }
}