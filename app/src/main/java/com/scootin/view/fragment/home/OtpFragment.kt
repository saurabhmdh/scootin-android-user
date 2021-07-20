package com.scootin.view.fragment.home

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentSendOtpBinding
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.util.constants.Validation
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OtpFragment: BaseFragment(R.layout.fragment_send_otp) {

    private var binding by autoCleared<FragmentSendOtpBinding>()
    private val viewModel: LoginViewModel by viewModels()
    private val args: OtpFragmentArgs by navArgs()
    private val mobile by lazy {
        args.mobileNo
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSendOtpBinding.bind(view)
        binding.mobile.text = mobile
        binding.txtResendOtp.isClickable=false
        timer.start()

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.loginComplete.observe(viewLifecycleOwner) { networkResponse ->
            when (networkResponse?.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(context, R.string.error_message_invalid_otp, Toast.LENGTH_SHORT)
                        .show()
                }
                Status.SUCCESS -> {
                    dismissLoading()
                    Timber.i("Successful response ${networkResponse.data}")
                    networkResponse.data?.let {
                        viewModel.saveUserInfo(it)
                        AppHeaders.updateUserData(it)
                        findNavController().navigate(OtpFragmentDirections.actionOtpToServiceArea())
                    }
                }
            }
        }

        viewModel.requestOTPComplete.observe(viewLifecycleOwner){ networkResponse ->
            when (networkResponse?.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    dismissLoading()
                    val text = getString(
                        R.string.success_message_otp,
                        mobile
                    )
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val otp = binding.otp.text.toString()

            if (otp.isEmpty() || Validation.REGEX_VALID_OTP.matcher(otp).matches().not()) {
                Toast.makeText(context, R.string.error_message_invalid_otp, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.doLogin(mobile, otp)
        }

        binding.txtResendOtp.setOnClickListener {

            binding.txtResendOtp.isClickable = false
                timer.start()
                viewModel.sendOTP(mobile)

        }
    }

    private val timer = object : CountDownTimer(30000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            if(!isVisible) {
                return
            }
            val time = millisUntilFinished / 1000
            if (time == 0L) {
                binding.timer.text = ""
            } else {
                binding.timer.text = "${time} sec."
            }
        }

        override fun onFinish() {
            if(!isVisible) {
                return
            }
            binding.txtResendOtp.isClickable = true
            binding.timer.text = ""
        }
    }
}