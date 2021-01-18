package com.scootin.view.fragment.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentLoginBinding
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.util.constants.AppConstants
import com.scootin.util.constants.Validation
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private var binding by autoCleared<FragmentLoginBinding>()
    private val viewModel: LoginViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        updateUI()
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
                        openHomeScreen()
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
                        binding.editTextPhnNo.text.toString()
                    )
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                    binding.sendOtp.isEnabled = false
                }
            }
        }

        binding.signIn.setOnClickListener {
            val mobileNumber = binding.editTextPhnNo.text.toString()
            val otp = binding.editTextOTP.text.toString()

            if (mobileNumber.isEmpty() || Validation.REGEX_VALID_MOBILE_NUMBER.matcher(mobileNumber).matches().not()) {
                Toast.makeText(context, R.string.error_message_invalid_mobile, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (otp.isEmpty() || Validation.REGEX_VALID_OTP.matcher(otp).matches().not()) {
                Toast.makeText(context, R.string.error_message_invalid_otp, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if(binding.termAccepted.isChecked.not()) {
                Toast.makeText(context, R.string.error_message_select_terms, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.doLogin(mobileNumber, otp)
        }

        binding.sendOtp.setOnClickListener {
            val mobileNumber = binding.editTextPhnNo.text.toString()
            if (mobileNumber.isEmpty() || Validation.REGEX_VALID_MOBILE_NUMBER.matcher(mobileNumber).matches().not()) {
                Toast.makeText(context, R.string.error_message_invalid_mobile, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.sendOTP(mobileNumber)
            }
        }
        binding.senAgainLoginOtp.setOnClickListener {
            val mobileNumber = binding.editTextPhnNo.text.toString()
            if (mobileNumber.isEmpty() || Validation.REGEX_VALID_MOBILE_NUMBER.matcher(mobileNumber).matches().not()) {
                Toast.makeText(context, R.string.error_message_invalid_mobile, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.sendOTP(mobileNumber)
            }
        }
    }

    private fun updateUI() {
        val stringData = getString(R.string.login_terms_and_condition)
        val wordtoSpan = SpannableString(stringData)

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                findNavController().navigate(LoginFragmentDirections.actionLoginToWebview(AppConstants.TERMS_AND_CONDITION))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
            }
        }

        val remainingText : ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                binding.termAccepted.isChecked = !binding.termAccepted.isChecked
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        wordtoSpan.setSpan(remainingText, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordtoSpan.setSpan(clickableSpan, 15, stringData.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.termAcceptedText.movementMethod = LinkMovementMethod.getInstance()

        binding.termAcceptedText.text = wordtoSpan
    }

    private fun openHomeScreen() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }


}