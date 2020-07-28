package com.scootin.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import com.scootin.R
import com.scootin.databinding.FragmentLoginBinding
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity

class LoginFragment:Fragment(R.layout.fragment_login) {
    private var binding by autoCleared<FragmentLoginBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.signIn.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }
    }
}