package com.scootin.view.fragment.wallet

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.razorpay.Checkout
import com.scootin.R
import com.scootin.databinding.FragmentWalletMyBinding
import com.scootin.network.AppExecutors
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.WalletAdapter
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyWalletFragment : Fragment(R.layout.fragment_wallet_my) {

    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var walletAdapter: WalletAdapter
    private var binding by autoCleared<FragmentWalletMyBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWalletMyBinding.bind(view)
        Checkout.preload(context)
        setProductAdapter()
        viewModel.listTransaction(1)
        viewModel.listTransactionLiveData.observe(viewLifecycleOwner, Observer {
            walletAdapter.submitList(it.body())
        })

        viewModel.addMoney.observe(viewLifecycleOwner, Observer {
            Timber.i("addMoney = ${it.body()}")
        })

        binding.addMoney.setOnClickListener {
            //Let me start payment directly
            startPayment(binding.addMoneyEditText.text.toString())
        }

        viewModel.verifyPaymentRequestLiveData.observe(viewLifecycleOwner, Observer {
            Timber.i("wallet verifyPaymentRequestLiveData = ${it}")
        })
    }

    private fun setProductAdapter() {
        walletAdapter = WalletAdapter(appExecutors)

        binding.productList.apply {
            adapter = walletAdapter
        }

    }

    private fun startPayment(price: String) {
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","Scootin Inc")
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("image","https://image-res.s3.ap-south-1.amazonaws.com/scootin-logo.png")
            options.put("theme.color", "#E90000")
            options.put("currency","INR");
            // options.put("order_id", "order_DBJOWzybf0sJbb");

            options.put("amount",price)//pass amount in currency subunits



            val prefill = JSONObject()
//           prefill.put("email","sumit.gupta@example.com")
            prefill.put("contact", AppHeaders.userMobileNumber)

            options.put("prefill", prefill)
            co.open(activity, options)

            //Razorpay will return 3 values.. Which we need to check
            //capture-payment
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    fun onPaymentSuccess(razorpayPaymentId: String?){
        Timber.i("onPaymentSuccess = ${razorpayPaymentId}")
        viewModel.verifyPaymentRequest(VerifyAmountRequest(razorpayPaymentId))
    }
}