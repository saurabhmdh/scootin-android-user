package com.scootin.view.fragment.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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


    @Inject
    lateinit var appExecutors: AppExecutors

    var promoCode: String = ""

    var orderId: List<Long>? = emptyList()
    var address: AddressDetails? = null

    //We need to load order in-order to get more information about order
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaymenttStatusBinding.bind(view)
        paymentModeSelector()
        setListener()
    }

    private fun setListener() {
        viewModel.loadPaymentInfo("")
        showLoading()
        viewModel.paymentInfo.observe(viewLifecycleOwner) {
            dismissLoading()
            if (it.isSuccessful) {
                dismissLoading()
                val data = it.body()
                binding.data = data
                if (data?.couponDiscount != 0.0) {
                    binding.promoApplied.visibility = View.VISIBLE
                    binding.discountApplied.text = "Discount Applied (${promoCode})"
                    binding.applyPromoButton.visibility=View.GONE
                    binding.removePromoButton.visibility=View.VISIBLE

                } else {
                    binding.promoApplied.visibility = View.GONE
                    binding.applyPromoButton.visibility=View.VISIBLE
                    binding.removePromoButton.visibility=View.GONE
                }

            } else {
                dismissLoading()
                Toast.makeText(context, "Invalid Coupon code", Toast.LENGTH_SHORT).show()
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
            }
        }


        binding.confirmButton.setOnClickListener {
            var mode="null"
            if(binding.cod.isSelected){
                mode="CASH"
            }
            else if(binding.payByCard.isSelected||binding.netBanking.isSelected||binding.upi.isSelected){
                mode="ONLINE"
            }

            else{
                Toast.makeText(requireContext(), "Please select a payment mode", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (address == null) {
                Toast.makeText(requireContext(), "Please add a address", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val alertDialog = context?.let { it1 -> MaterialAlertDialogBuilder(it1) }

            alertDialog?.setMessage(R.string.addressCheck)


            alertDialog?.setPositiveButton("Confirm") { dialogInterface, which ->


            showLoading()

               // val mode="CASH"

            viewModel.userConfirmOrder(AppHeaders.userID, OrderRequest(mode, AppHeaders.serviceAreaId, address!!.id, promoCode)).observe(viewLifecycleOwner) {
                when(it.status) {
                    Status.SUCCESS -> {
                        Timber.i(" data ${it.data}")
                        if (it.data.isNullOrEmpty()) {
                            Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show()
                            return@observe
                        }

                        orderId = it.data.map { it.id }
                        val totalAmount = it.data.sumByDouble { it.paymentDetails.totalAmount.orDefault(0.0) }

                        Timber.i("order id $orderId")
                        if (it.data.first().paymentDetails.paymentMode.equals("ONLINE")) {
                            val total = totalAmount.times(100)
                            startPayment(it.data.first().paymentDetails.orderReference.orEmpty(), total)
                            dismissLoading()
                        } else {
                            dismissLoading()
                            orderId?.toLongArray()?.let { orders->
                                findNavController().navigate(CardPaymentPageFragmentDirections.orderConfirmationPage(orders))
                            }
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

            alertDialog?.setNegativeButton("Cancel") { dialogInterface, which ->

            }



            alertDialog?.setCancelable(false)

            alertDialog?.show()
        }

        binding.applyPromoButton.setOnClickListener {

            if (binding.couponEdittext.text?.toString()?.isEmpty() == true) {
                Toast.makeText(context, "Please enter a valid coupon code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            else{
//                binding.applyPromoButton.visibility=View.GONE
//                binding.removePromoButton.visibility=View.VISIBLE
//            }
            promoCode = binding.couponEdittext.text!!.toString()
            showLoading()
            viewModel.loadPaymentInfo(promoCode)
        }

        binding.removePromoButton.setOnClickListener {
            viewModel.loadPaymentInfo("")
            binding.promoApplied.visibility = View.GONE
            binding.applyPromoButton.visibility=View.VISIBLE
            binding.removePromoButton.visibility=View.GONE
            binding.couponEdittext.getText()?.clear()
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }


        binding.dropAddress.setOnClickListener {
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

    private fun paymentModeSelector(){
        binding.payByCard.setOnClickListener {
            binding.payByCard.isSelected=true
            binding.netBanking.isSelected=false
            binding.upi.isSelected=false
            binding.cod.isSelected=false
        }
        binding.netBanking.setOnClickListener {
            binding.payByCard.isSelected=false
            binding.netBanking.isSelected=true
            binding.upi.isSelected=false
            binding.cod.isSelected=false
        }
        binding.upi.setOnClickListener {
            binding.payByCard.isSelected=false
            binding.netBanking.isSelected=false
            binding.upi.isSelected=true
            binding.cod.isSelected=false
        }
        binding.cod.setOnClickListener {
            binding.payByCard.isSelected=false
            binding.netBanking.isSelected=false
            binding.upi.isSelected=false
            binding.cod.isSelected=true
        }
    }


    private fun startPayment(orderReferenceId: String, price: Double) {
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", AppConstants.APPLICATION_NAME)
            options.put("image", AppConstants.RAZORPAY_APP_IMAGE)
            options.put("theme.color", AppConstants.RAZORPAY_THEME_COLOR)
            options.put("currency", AppConstants.RAZORPAY_CURRENCY)
            options.put("amount", price)
            options.put("order_id", orderReferenceId)
            val prefill = JSONObject()
            prefill.put("email",AppConstants.RAZORPAY_EMAIL)
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
        viewModel.verifyPayment(VerifyAmountRequest(razorpayPaymentId)).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    dismissLoading()
                    orderId?.toLongArray()?.let { orders->
                        findNavController().navigate(CardPaymentPageFragmentDirections.orderConfirmationPage(orders))
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(activity, "There is network issue, please try after some time", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}