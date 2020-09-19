package com.scootin.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.scootin.R
import com.scootin.databinding.FragmentLoginBinding
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity
import com.scootin.viewmodel.login.LoginViewModel
import androidx.lifecycle.observe
import com.rakuten.travel.consumer.extensions.updateVisibility
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var binding by autoCleared<FragmentLoginBinding>()
    private val viewModel: LoginViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)


        viewModel.loginComplete.observe(viewLifecycleOwner) {networkResponse ->
            when (networkResponse?.status) {
                Status.LOADING -> {
                    //TODO: Showing loading..
                }
                Status.ERROR -> {
                    Toast.makeText(context, "OTP is not valid", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    Timber.i("Successful response ${networkResponse.data}")
                    networkResponse.data?.let {
                        viewModel.saveUserInfo(it)
                        AppHeaders.updateUserData(it)
                        openHomeScreen()
                    }
                }
            }
        }

        viewModel.requestOTPComplete.observe(viewLifecycleOwner){ networkResponse ->
            when (networkResponse?.status) {
                Status.LOADING -> {
                    //TODO: Showing loading..
                }
                Status.ERROR -> {
                    Toast.makeText(context, "Please try after sometime", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    Toast.makeText(context, "Please input your OTP", Toast.LENGTH_SHORT).show()
                    binding.sendOtp.updateVisibility(false)
                    binding.signIn.updateVisibility(true)
                }

            }
        }

        binding.signIn.setOnClickListener {
            viewModel.doLogin(binding.editTextPhnNo.text.toString(), binding.editTextOTP.text.toString())
        }

        binding.sendOtp.setOnClickListener {
            //Get the phone no
            val mobileNumber = binding.editTextPhnNo.text.toString()
            if (mobileNumber.length >= 10) {
                viewModel.sendOTP(mobileNumber)
            } else {
                Toast.makeText(context, "Please provide valid phone number", Toast.LENGTH_SHORT).show()
            }

        }

    }
    private fun openHomeScreen() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }


}