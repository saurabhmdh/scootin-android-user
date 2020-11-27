package com.scootin.view.fragment.cart

import android.content.Intent
import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.razorpay.Checkout
import com.scootin.R
import com.scootin.databinding.FragmentPaymenttStatusBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.extensions.getNavigationResult
import com.scootin.extensions.orDefault
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.OrderRequest
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.network.response.AddressDetails
import com.scootin.util.UtilUIComponent
import com.scootin.util.constants.AppConstants
import com.scootin.util.constants.IntentConstants
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

    var promoCode: String = ""

    private val orderId by lazy {
        args.orderId
    }

    var address: AddressDetails? = null

    //We need to load order in-order to get more information about order
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaymenttStatusBinding.bind(view)

        setListener()
    }

    private fun setListener() {
        viewModel.loadOrder(orderId)

        viewModel.orderInfo.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.data = it.data

                    if (it.data?.orderDetails?.paymentDetails?.promoCodeApplied == true) {
                        binding.promoApplied.visibility = View.VISIBLE
                        binding.discountApplied.text = "Discount Applied (${promoCode})"
                        //promoCode
                    } else {
                        binding.promoApplied.visibility = View.GONE
                    }
                }
                Status.ERROR -> {
                    //Show error and move back
                }
            }
        }

        //Lets load all address if there is no address then ask to add, incase there is
        viewModel.loadAllAddress().observe(viewLifecycleOwner) {
            //find defaultAddress..
            if (it.isSuccessful) {

                if (address != null) {
                    Timber.i("We have address from previous fragment $address")
                    return@observe
                }
                address = it.body()?.first { it.hasDefault }
                Timber.i("We found address ${address}")
                address?.let {
                    binding.editDropAddress.text = UtilUIComponent.setOneLineAddress(address)
                }

            } else {
                //We need to
            }
        }


        binding.confirmButton.setOnClickListener {
            if (address == null) {
                Toast.makeText(requireContext(), "Please add a address", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val mode = when(binding.radioGroup.getCheckedRadioButtonPosition()) {
                0 -> {"ONLINE"}
                1 -> {"CASH"}
                else -> {""}
            }
            showLoading()

            viewModel.userConfirmOrder(orderId.toString(), AppHeaders.userID, OrderRequest(mode, address!!.id)).observe(viewLifecycleOwner) {
                when(it.status) {
                    Status.SUCCESS -> {

                        Timber.i(" data ${it.data}")
                        dismissLoading()
                        if (it.data?.paymentDetails?.paymentMode.equals("ONLINE")) {
                            val total = it.data?.paymentDetails?.totalAmount.orDefault(0.0) * 100
                            startPayment(it.data?.paymentDetails?.orderReference.orEmpty(), total)
                        } else {
                            findNavController().navigate(CardPaymentPageFragmentDirections.orderConfirmationPage(orderId))
                        }
                    }
                    Status.ERROR -> {
                        dismissLoading()
                    }
                    Status.LOADING -> {}
                }
            }
        }

        binding.applyPromoButton.setOnClickListener {
            if (binding.couponEdittext.text?.toString()?.isEmpty() == true) {
                Toast.makeText(context, "Please enter valid coupon code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.promCodeRequest(orderId.toString(), binding.couponEdittext.text.toString()).observe(viewLifecycleOwner) {
                if (it.isSuccessful) {
                    promoCode = binding.couponEdittext.text.toString()
                    viewModel.loadOrder(orderId)
                    Toast.makeText(context, "Coupon has been applied.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Invalid Coupon code", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }


        binding.editDropAddress.setOnClickListener {
            findNavController().navigate(IntentConstants.openAddressPage())
        }

        getNavigationResult()?.observe(viewLifecycleOwner) {
            updateAddressData(it)
        }
    }
    private fun updateAddressData(calendarData: String) {
        val result =
            Gson().fromJson<AddressDetails>(calendarData, object : TypeToken<AddressDetails>() {}.type) ?: return
        address = result
        binding.editDropAddress.text = UtilUIComponent.setOneLineAddress(address)
        Timber.i("update the address $result")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.i("data := ${data}")
    }

    fun onPaymentSuccess(razorpayPaymentId: String?){
        Timber.i("onPaymentSuccess = ${razorpayPaymentId}")
        viewModel.verifyPayment(VerifyAmountRequest(razorpayPaymentId)).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    //Need some direction to move
                    findNavController().navigate(CardPaymentPageFragmentDirections.orderConfirmationPage(orderId))
                }
                Status.ERROR -> {}
            }
        }
    }
}