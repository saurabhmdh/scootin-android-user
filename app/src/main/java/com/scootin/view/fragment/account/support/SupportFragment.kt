package com.scootin.view.fragment.account.support

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.scootin.R
import com.scootin.databinding.CustomerSupportBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.SupportFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SupportFragment : Fragment(R.layout.customer_support) {

    private var binding by autoCleared<CustomerSupportBinding>()
    private val viewModel: SupportFragmentViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CustomerSupportBinding.bind(view)
        binding.mailCustomerSupport.setOnClickListener {
/* Create the Intent */
            val emailIntent = Intent(android.content.Intent.ACTION_SEND)

/* Fill it with Data */
            emailIntent.setType("plain/text")
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf("support@scootin.co.in"))
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Regarding order ID ${binding.appCompatEditText.text.toString()}" )
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text")
/* Send it off to the Activity-Chooser */
            requireContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."))
        }
        binding.activeCallSupport.setOnClickListener {
            val mobileNumber = binding.activeCallSupport.text
            val intent = Intent()
            intent.action = Intent.ACTION_DIAL // Action for what intent called for
            intent.data = Uri.parse("tel: $mobileNumber") // Data with intent respective action on intent
            startActivity(intent)
        }
    }

}