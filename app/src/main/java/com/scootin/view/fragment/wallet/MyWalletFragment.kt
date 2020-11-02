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
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddMoneyWallet
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.WalletAdapter
import com.scootin.viewmodel.delivery.CategoriesViewModel
import com.scootin.viewmodel.wallet.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyWalletFragment : Fragment(R.layout.fragment_wallet_my) {

    private val viewModel: WalletViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var walletAdapter: WalletAdapter
    private var binding by autoCleared<FragmentWalletMyBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWalletMyBinding.bind(view)
        //Checkout.preload(context)
        setProductAdapter()

        loadData()

        binding.addMoney.setOnClickListener {
            handleAddMoney()
        }
    }

    private fun loadData() {
        viewModel.listTransaction(AppHeaders.userID).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    walletAdapter.submitList(it.data)
                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
    }

    private fun handleAddMoney() {
        val input = binding.addMoneyEditText.text.toString().toDoubleOrNull() ?: return
        viewModel.addMoneyToWallet(AppHeaders.userID, AddMoneyWallet(input)).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.orderId?.let {orderId->
                        startPayment(input * 100, orderId)
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }

    }

    private fun setProductAdapter() {
        walletAdapter = WalletAdapter(appExecutors)

        binding.productList.apply {
            adapter = walletAdapter
        }

    }

    private fun startPayment(price: Double, orderId: String) {
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","Scootin Inc")
            options.put("image","https://image-res.s3.ap-south-1.amazonaws.com/scootin-logo.png")
            options.put("theme.color", "#E90000")
            options.put("currency","INR")
            options.put("order_id", orderId)
            options.put("amount", price)

            val prefill = JSONObject()
            prefill.put("email","support@scootin.co.in")
            prefill.put("contact", AppHeaders.userMobileNumber)

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    fun onPaymentSuccess(razorpayPaymentId: String?) {
        Timber.i("onPaymentSuccess = ${razorpayPaymentId}")
        viewModel.verifyWalletPayment(AppHeaders.userID, VerifyAmountRequest(razorpayPaymentId)).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.addMoneyEditText.setText("")
                    loadData()
                    Toast.makeText(requireContext(), "Money added to wallet", Toast.LENGTH_LONG).show()
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }
}