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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentUserDetailBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.network.api.Status
import com.scootin.network.request.RequestRegisterUser
import com.scootin.util.constants.AppConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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
        updateListeners()
    }

    private fun updateListeners() {
        binding.txtEdit.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.mobile.text = mobile

        binding.datePicker.maxDate = Calendar.getInstance().time.time

        binding.btnSendOtp.setOnClickListener {

            if (!isValidated()) return@setOnClickListener
            val requests = collectData()
            showLoading()

            viewModel.registerUser(requests).observe(viewLifecycleOwner) {

                when(it.status) {
                    Status.SUCCESS -> {
                        dismissLoading()
                        findNavController().navigate(UserDetailFragmentDirections.actionUserDetailToOtp(mobile))
                    }
                    Status.ERROR -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "There is problem while saving data", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING ->{}
                }
            }
        }
    }

    private fun collectData(): RequestRegisterUser {
        val city = binding.city.text?.toString() ?: ""
        val name = binding.fullName.text?.toString() ?: ""

        val dateOfBirth = "${binding.datePicker.dayOfMonth}-${binding.datePicker.month}-${binding.datePicker.year}"

        val gender = when(binding.radioGroup.getCheckedRadioButtonPosition()) {
            1-> "FEMALE"
            else -> "MALE"
        }
        return RequestRegisterUser(city, dateOfBirth, name, gender, mobile)
    }

    private fun isValidated(): Boolean {
        if (binding.fullName.text?.toString().isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Name can't be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.city.text?.toString().isNullOrEmpty()) {
            Toast.makeText(requireContext(), "City can't be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        when(binding.radioGroup.getCheckedRadioButtonPosition()) {
            0,1 -> {}
            else -> {
                Toast.makeText(requireContext(), "Please select gender", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        if (binding.termAccepted.isChecked.not()) {
            Toast.makeText(requireContext(), R.string.error_message_select_terms, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
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