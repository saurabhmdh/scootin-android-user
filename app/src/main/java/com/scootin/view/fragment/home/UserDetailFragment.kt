package com.scootin.view.fragment.home

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentSendOtpBinding
import com.scootin.databinding.FragmentUserDetailBinding
import com.scootin.util.constants.AppConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment: BaseFragment(R.layout.fragment_user_detail) {
    private var binding by autoCleared<FragmentUserDetailBinding>()
    private val viewModel: LoginViewModel by viewModels()
    private val args: UserDetailFragmentArgs by navArgs()
    private val mobile by lazy {
        args.mobileNo
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserDetailBinding.bind(view)
        updateUI()
        binding.txtEdit.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.mobile.setText(mobile)
        binding.btnSendOtp.setOnClickListener {
           findNavController().navigate(UserDetailFragmentDirections.actionUserDetailToOtp(mobile))
        }
    }
    private fun updateUI() {
        val stringData = getString(R.string.login_terms_and_condition)
        val wordtoSpan = SpannableString(stringData)

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                findNavController().navigate(UserDetailFragmentDirections.actionUserDetailToWebview(
                    AppConstants.TERMS_AND_CONDITION))
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

        wordtoSpan.setSpan(remainingText, 0, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordtoSpan.setSpan(clickableSpan, 26, stringData.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.termAcceptedText.movementMethod = LinkMovementMethod.getInstance()

        binding.termAcceptedText.text = wordtoSpan
    }
}