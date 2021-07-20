package com.scootin.view.fragment.home

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentLoginBinding
import com.scootin.util.constants.AppConstants
import com.scootin.util.constants.Validation
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private var binding by autoCleared<FragmentLoginBinding>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        updateUI()
        setListeners()
    }

    private fun setListeners() {
        binding.btnContinue.setOnClickListener {
            val mobileNumber = binding.editTextPhnNo.text.toString()


            if (mobileNumber.isEmpty() || Validation.REGEX_VALID_MOBILE_NUMBER.matcher(mobileNumber)
                    .matches().not()
            ) {
                Toast.makeText(context, R.string.error_message_invalid_mobile, Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            findNavController().navigate(
                LoginFragmentDirections.actionLoginToUserDetail(
                    mobileNumber
                )
            )
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
               // binding.termAccepted.isChecked = !binding.termAccepted.isChecked
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        wordtoSpan.setSpan(remainingText, 0, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordtoSpan.setSpan(clickableSpan, 26, stringData.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.termAcceptedText.movementMethod = LinkMovementMethod.getInstance()

        binding.termAcceptedText.text = wordtoSpan
    }
}