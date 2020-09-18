package com.scootin.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.scootin.R
import com.scootin.databinding.FragmentLoginBinding
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity
import com.scootin.viewmodel.login.LoginViewModel
import androidx.lifecycle.observe
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import dagger.hilt.android.AndroidEntryPoint

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
                    //TODO: Show toast..
                }
                Status.SUCCESS -> {
                    networkResponse.data?.let {
                        viewModel.saveUserInfo(it)
                        AppHeaders.updateUserData(it)
                        openHomeScreen()
                    }
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
                viewModel.requestOTP(mobileNumber)
            }
        }

    }
    private fun openHomeScreen() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }


}