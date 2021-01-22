package com.scootin.view.fragment.account.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.razorpay.Checkout
import com.scootin.R
import com.scootin.databinding.FragmentChangePaymentMethodBinding
import com.scootin.databinding.FragmentPaymentBinding
import com.scootin.databinding.FragmentPaymenttStatusBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.extensions.getNavigationResult
import com.scootin.extensions.orDefault
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.OrderRequest
import com.scootin.network.request.PromoCodeRequest
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.network.response.AddressDetails
import com.scootin.util.UtilUIComponent
import com.scootin.util.constants.IntentConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.BaseFragment
import com.scootin.view.fragment.cart.CardPaymentPageFragmentDirections
import com.scootin.viewmodel.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class ChangePaymentMethodFragment: BaseFragment(R.layout.fragment_change_payment_method) {
    private var binding by autoCleared<FragmentChangePaymentMethodBinding>()
    private val viewModel: PaymentViewModel by viewModels()

    private val args: ChangePaymentMethodFragmentArgs by navArgs()

    @Inject
    lateinit var appExecutors: AppExecutors

    private val orderId by lazy {
        args.orderId
    }

    //We can use same fragment to make payment of citywide case..
    private val orderType by lazy {
        args.orderType
    }


    //We need to load order in-order to get more information about order
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangePaymentMethodBinding.bind(view)

        setListener()
    }


    private fun setListener() {
        viewModel.loadOrder(orderId.toLong())

        //Let me load new order

        when (orderType) {
            "DIRECT" -> {
                addDirectOrderListener()
            }
            "NORMAL" -> {
                addNormalOrderListener()
            }
            "CITYWIDE" -> {
                addCityWideOrderListener()
            }
        }

        binding.confirmButton.setOnClickListener {
            val mode = when (binding.radioGroup.getCheckedRadioButtonPosition()) {
                0 -> {
                    "ONLINE"
                }
                else -> {
                    ""
                }
            }
            showLoading()
            when (orderType) {
                "DIRECT" -> {
                    addUserConfirmOrderDirectListener(mode)
                }
                "CITYWIDE" -> {
                    addUserConfirmOrderCityWideListener(mode)
                }
                "NORMAL" -> {
                    addUserConfirmOrderListener(mode)
                }
            }
        }

//        binding.applyPromoButton.setOnClickListener {
//            if (binding.couponEdittext.text?.toString()?.isEmpty() == true) {
//                Toast.makeText(context, "Please enter valid coupon code", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            val promoCode = binding.couponEdittext.text.toString()
//            showLoading()
//            viewModel.applyPromo(orderId, AppHeaders.userID, PromoCodeRequest(promoCode, orderType))
//                .observe(viewLifecycleOwner) {
//                    if (it.isSuccessful) {
//                        dismissLoading()
//                        binding.discountApplied.text = "Discount Applied (${promoCode})"
//                        binding.promoApplied.visibility = View.VISIBLE
//                        viewModel.loadOrder(orderId.toLong())
//                    } else {
//                        dismissLoading()
//                        Toast.makeText(requireContext(), "Invalid promocode!!", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//        }

        binding.back.setOnClickListener { findNavController().popBackStack() }
    }

    private fun addUserConfirmOrderListener(mode: String) {
        viewModel.changePaymentMethod(orderId).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {

                    Timber.i("order id $orderId")
                    if (it.data?.paymentDetails?.paymentMode.equals("ONLINE")) {
                        val total = it.data?.paymentDetails?.totalAmount.orDefault(0.0) * 100
                        startPayment(it.data?.paymentDetails?.orderReference.orEmpty(), total)
                        dismissLoading()
                    } else {
                        dismissLoading()
                        findNavController().popBackStack()
                    }
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(requireContext(), "There is network issue, please try after some time", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {}
                }
            }
    }


    private fun addUserConfirmOrderCityWideListener(mode: String) {
        viewModel.userConfirmOrderCityWide(orderId, OrderRequest(mode, AppHeaders.serviceAreaId))
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        Timber.i(" data ${it.data}")
                        Timber.i("order id $orderId")
                        dismissLoading()
                        if (it.data?.paymentDetails?.paymentMode.equals("ONLINE")) {
                            val total = it.data?.paymentDetails?.totalAmount.orDefault(0.0) * 100
                            startPayment(it.data?.paymentDetails?.orderReference.orEmpty(), total)
                        }
                    }
                    Status.ERROR -> {
                        dismissLoading()
                    }
                    Status.LOADING -> {
                    }
                }
            }
    }

    private fun addDirectOrderListener() {
        viewModel.directOrderInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("${it.data}")
                    it.data?.let { orderDetail ->
                        binding.data = orderDetail.paymentDetails
                    }
                }
            }

        }
    }

    private fun addNormalOrderListener() {
        viewModel.normalOrderInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("${it.data}")
                    it.data?.let { orderDetail ->
                        binding.data = orderDetail.orderDetails.paymentDetails
                    }
                }
            }

        }
    }

    private fun addUserConfirmOrderDirectListener(mode: String) {
        viewModel.userConfirmOrderDirect(orderId, OrderRequest(mode, AppHeaders.serviceAreaId))
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        Timber.i(" data ${it.data}")
                        Timber.i("order id $orderId")
                        dismissLoading()
                        if (it.data?.paymentDetails?.paymentMode.equals("ONLINE")) {
                            val total = it.data?.paymentDetails?.totalAmount.orDefault(0.0) * 100
                            startPayment(it.data?.paymentDetails?.orderReference.orEmpty(), total)
                        }
                    }
                    Status.ERROR -> {
                        dismissLoading()
                    }
                    Status.LOADING -> {
                    }
                }
            }
    }

    private fun addCityWideOrderListener() {
        viewModel.citywideOrderInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("${it.data}")
                    it.data?.let { orderDetail ->
                        binding.data = orderDetail.paymentDetails
                    }
                }
            }

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
            prefill.put("email", "support@scootin.co.in")
            prefill.put("contact", AppHeaders.userMobileNumber)

            options.put("prefill", prefill)
            co.open(activity, options)

        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.i("data := ${data}")
    }

    fun onPaymentSuccess(razorpayPaymentId: String?) {
        Timber.i("onPaymentSuccess = ${razorpayPaymentId} $orderId")
        when (orderType) {
            "DIRECT" -> {
                verifyPaymentDirectListener(razorpayPaymentId)
            }
            "CITYWIDE" -> {
                verifyPaymentCityWideListener(razorpayPaymentId)
            }
            "NORMAL" -> {
                verifyPaymentNormalListener(razorpayPaymentId)
            }
        }

    }

    fun verifyPaymentNormalListener(razorpayPaymentId: String?){
        Timber.i("onPaymentSuccess = ${razorpayPaymentId} $orderId")
        viewModel.verifyPayment(VerifyAmountRequest(razorpayPaymentId)).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Payment successful", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                }
                Status.ERROR -> {
                    Toast.makeText(activity, "There is network issue, please try after some time", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun verifyPaymentDirectListener(razorpayPaymentId: String?) {
        viewModel.verifyPaymentDirect(VerifyAmountRequest(razorpayPaymentId))
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Payment successful", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                    }
                }
            }
    }

    private fun verifyPaymentCityWideListener(razorpayPaymentId: String?) {
        viewModel.verifyPaymentCityWide(VerifyAmountRequest(razorpayPaymentId))
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Payment successful", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                    }
                }
            }
    }
}