package com.scootin.view.fragment.account.support

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
            sendEmail("support@scootin.co.in", "Regarding order ID ${binding.editTextForOrderId.text.toString()}", "");
        }

        setupListener()

        binding.activeCallSupport.setOnClickListener {
            val mobileNumber = binding.phn.text
            val intent = Intent()
            intent.action = Intent.ACTION_DIAL // Action for what intent called for
            intent.data = Uri.parse("tel: $mobileNumber") // Data with intent respective action on intent
            startActivity(intent)
        }
    }

    private fun sendEmail(address: String, subject: String, body: String){
        val TO = arrayOf(address)
        val uri = Uri.parse(address)
            .buildUpon()
            .appendQueryParameter("subject", subject)
            .appendQueryParameter("body", body)
            .build()
        val emailIntent = Intent(Intent.ACTION_SENDTO, uri).apply {
            setData(Uri.parse("mailto:$address"))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            putExtra(Intent.EXTRA_EMAIL, TO)
        }
        ContextCompat.startActivity(requireContext(), Intent.createChooser(emailIntent, "Send mail..."), null)
    }

    private fun setupListener() {


        binding.submit.setOnClickListener {
            val callId = binding.editTextForOrderId.text?.toString()?.toIntOrNull() ?: 0
            if (callId == 0) {
                Toast.makeText(requireContext(), "Please enter valid order Id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.verifyOrderId(callId.toString()).observe(viewLifecycleOwner) {
                if (it.isSuccessful) {
                    binding.activeCallSupport.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Please enter valid order Id", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}