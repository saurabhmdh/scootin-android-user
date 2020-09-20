package com.scootin.view.fragment.home


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.view.activity.MainActivity
import com.scootin.viewmodel.home.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val viewModel: SplashViewModel by viewModels()

    private var timeout: Boolean = false
    private var dataLoaded: Boolean = false
    private var firstTime: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.firstLaunch().observe(viewLifecycleOwner, {
            dataLoaded = true
            firstTime = it
        })

        Handler().postDelayed({
            timeout = true
            tryToGoNext()
        }, 3000)
    }

    private fun gotoLoginFragment() {
        findNavController().navigate(
            SplashFragmentDirections.actionSplashToLogin(),
            NavOptions.Builder().setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .setPopUpTo(R.id.splash, true).build()
        )
    }

    private fun tryToGoNext() {
        Timber.i("tryToGoNext lets check the status ${canGoNextStep()}")
        if (canGoNextStep()) {
            if (firstTime) {
                gotoLoginFragment()
            } else {
                openHomeScreen()
            }
        }
    }

    private fun canGoNextStep() = timeout && dataLoaded

    private fun openHomeScreen() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }
}