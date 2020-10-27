package com.scootin.view.fragment.cart

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentPaymenttStatusBinding
import com.scootin.network.AppExecutors
import com.scootin.network.request.PlaceOrderRequest
import com.scootin.network.response.placeOrder.PlaceOrderResponse
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.AddCartItemAdapter
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CardPaymentPageFragment : Fragment(R.layout.fragment_paymentt_status) {
    private var binding by autoCleared<FragmentPaymenttStatusBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var addCartItemAdapter: AddCartItemAdapter
    var paymentMode ="CASH"
    var orderId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaymenttStatusBinding.bind(view)
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
            // TODO launch payment screen if paymentMode is online
            if(paymentMode == "ONLINE"){
                // launch payment screen
//                findNavController().navigate()
            } else {
                // launch success screen
            }
        })

        binding.applyPromoButton.setOnClickListener {
            viewModel.promCodeRequest( mapOf("orderID" to orderId, "promocode" to binding.couponEdittext.text.toString()))
        }

        viewModel.promCodeRequestLiveData.observe(viewLifecycleOwner, Observer {
            Timber.i("applyCoupon = ${it}")
        })
    }

    private fun callPaymentUiFunction(response: PlaceOrderResponse?) {
        orderId = response?.id.toString()
    }
}