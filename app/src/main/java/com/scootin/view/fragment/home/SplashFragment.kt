package com.scootin.view.fragment.home


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.view.activity.MainActivity
import com.scootin.view.activity.SplashActivity
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

        Timber.i("onViewCreated..")

        viewModel.firstLaunch().observe(viewLifecycleOwner, {
            dataLoaded = true
            firstTime = it
            tryToGoNext()
            Timber.i("firstLaunch() $timeout $firstTime")
        })

        Handler().postDelayed({
            timeout = true
            tryToGoNext()
            Timber.i("Handler() $timeout $firstTime")
        }, 3000)
    }

    private fun gotoLoginFragment() {
        findNavController().navigate(
            SplashFragmentDirections.actionSplashToLogin(),
            NavOptions.Builder().setEnterAnim(R.anim.default_enter_anim)
                .setExitAnim(R.anim.default_exit_anim)
                .setPopEnterAnim(R.anim.default_pop_enter_anim)
                .setPopExitAnim(R.anim.default_pop_exit_anim)
                .setPopUpTo(R.id.splash, true).build()
        )
    }

    private fun tryToGoNext() {
        Timber.i("tryToGoNext lets check the status ${canGoNextStep()}")
        if (canGoNextStep()) {

            val isRunning = activity as SplashActivity?
            if (isRunning?.isRunning() == true) {
                Timber.i("Update is going on hence no action..")
                return
            }

            if (firstTime) {
                gotoLoginFragment()
            } else {
                openHomeScreen()
            }
        }
    }

    private fun canGoNextStep() = timeout && dataLoaded

    private fun openHomeScreen() {
        lifecycleScope.launchWhenResumed {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
            activity?.finish()
        }
    }
}