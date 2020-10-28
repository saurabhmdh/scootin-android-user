package com.scootin.view.fragment.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.razorpay.Checkout
import com.scootin.R
import com.scootin.databinding.FragmentPaymenttStatusBinding
import com.scootin.network.AppExecutors
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.PlaceOrderRequest
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.network.response.placeOrder.PlaceOrderResponse
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CardPaymentPageFragment : Fragment(R.layout.fragment_paymentt_status) {
    private var binding by autoCleared<FragmentPaymenttStatusBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    var paymentMode = "ONLINE"
    var orderId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaymenttStatusBinding.bind(view)
        Checkout.preload(context)
        setListener()
        viewModel.placeOrder.postValue(PlaceOrderRequest(7553, false))
    }

    private fun setListener() {

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

            val button = group.findViewById<RadioButton>(checkedId)
            Timber.i("tag = ${button.tag}")
            paymentMode = button.tag.toString()
        }

        binding.confirmButton.setOnClickListener {
            viewModel.orderRequest(mapOf("paymentMode" to paymentMode, "orderId" to orderId))
        }

        viewModel.placeOrderLiveData.observe(viewLifecycleOwner, Observer {
            Timber.i("placeOrder = ${it}")
            // call payment UI

            val data = it.body()
            callPaymentUiFunction(data)
        })

        viewModel.orderRequestLiveData.observe(viewLifecycleOwner, Observer {
            Timber.i("orderRequest = ${it}")
            val response = it.body()
            orderId = "${response?.id}"
            // TODO launch payment screen if paymentMode is online
            if (paymentMode == "ONLINE") {
                // launch payment screen
                startPayment(response?.paymentDetails?.orderReference.orEmpty())
            } else {
                // launch success screen
            }
        })

        binding.applyPromoButton.setOnClickListener {
            viewModel.promCodeRequest(
                mapOf(
                    "orderID" to orderId,
                    "promocode" to binding.couponEdittext.text.toString()
                )
            )
        }

        viewModel.promCodeRequestLiveData.observe(viewLifecycleOwner, Observer {
            Timber.i("applyCoupon = ${it}")
        })

        viewModel.verifyPaymentRequestLiveData.observe(viewLifecycleOwner, Observer {
            Timber.i("verifyPaymentRequestLiveData = ${it}")
        })
    }

    private fun callPaymentUiFunction(response: PlaceOrderResponse?) {
        orderId = response?.id.toString()
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

//            options.put("amount","50000")//pass amount in currency subunits
            val prefill = JSONObject()
//           prefill.put("email","sumit.gupta@example.com")
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