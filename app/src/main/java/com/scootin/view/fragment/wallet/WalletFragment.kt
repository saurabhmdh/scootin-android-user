package com.scootin.view.fragment.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.scootin.R
import com.scootin.databinding.FragmentHomeBinding
import com.scootin.databinding.FragmentWalletBinding
import com.scootin.network.manager.AppHeaders
import com.scootin.util.fragment.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import timber.log.Timber

@AndroidEntryPoint
class WalletFragment: Fragment(R.layout.fragment_wallet){

    //Saurabh: Now I am using it for Razoypay testing..
    //https://razorpay.com/docs/payment-gateway/android-integration/standard/
    private var binding by autoCleared<FragmentWalletBinding>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWalletBinding.bind(view)

        Checkout.preload(context)

        //Let me start payment directly
        startPayment()
    }

   private fun startPayment() {
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

           options.put("amount","50000")//pass amount in currency subunits



           val prefill = JSONObject()
//           prefill.put("email","sumit.gupta@example.com")
           prefill.put("contact",AppHeaders.userMobileNumber)

           options.put("prefill", prefill)
           co.open(activity, options)

           //Razorpay will return 3 values.. Which we need to check
           //capture-payment
       }catch (e: Exception){
           Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
           e.printStackTrace()
       }
   }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.i("data := ${data}")
    }


}