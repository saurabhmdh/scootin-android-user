package com.scootin.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.scootin.R
import com.scootin.databinding.FragmentLoginBinding
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity
import kotlinx.android.synthetic.main.adapter_temple_item.view.*
import timber.log.Timber
import java.util.*

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var binding by autoCleared<FragmentLoginBinding>()
    val callbackManager = CallbackManager.Factory.create()
    private val RC_SIGN_IN: Int = 10001
    private val FB_SIGN_IN: Int = 64206

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        setGoogleLogin()
        facebookLoginCall()
        binding.signIn.setOnClickListener {
            openHomeScreen()
        }
    }

    private fun setGoogleLogin() {
        binding.google.setSize(SignInButton.SIZE_STANDARD)

        binding.google.setOnClickListener {
            signIn()
        }

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun facebookLoginCall() {
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

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(activity)
        Timber.i("login = $account")
        updateUI(account)
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        // open next screen
        openHomeScreen()
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
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
            FB_SIGN_IN -> {
                Timber.i("fb onactivity result call $requestCode")
                callbackManager.onActivityResult(requestCode, resultCode, data)
            }
            else -> Timber.e("$requestCode")
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            Timber.i("account = $account")
            updateUI(account)
        } catch (e: ApiException) {
            Timber.w("signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }
}