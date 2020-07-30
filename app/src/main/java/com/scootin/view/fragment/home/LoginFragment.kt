package com.scootin.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

import com.scootin.R
import com.scootin.databinding.FragmentLoginBinding
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity
import timber.log.Timber
import java.util.*

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var binding by autoCleared<FragmentLoginBinding>()
    val callbackManager = CallbackManager.Factory.create()
    private val EMAIL = "email"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.signIn.setOnClickListener {
            openHomeScreen()
        }


        binding.facebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("user_photos", "email", "user_birthday", "public_profile")
            )
        }

        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Timber.i("fb accesstoken = ${result?.accessToken}")
                    openHomeScreen()
                }

                override fun onCancel() {
                    Timber.e("fb cancel ")
                }

                override fun onError(error: FacebookException?) {
                    Timber.e("fb result error login")
                }

            })
    }

    private fun openHomeScreen() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        Timber.i("fb onactivity result call")
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}