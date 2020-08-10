package com.scootin.view.fragment.home


import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.viewmodel.home.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            startNextActivity()
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

    private fun startNextActivity() {
        Timber.i("Starting next activity..")
        viewModel.firstLaunch().observe(viewLifecycleOwner) {
            gotoLoginFragment()
        }
    }
}