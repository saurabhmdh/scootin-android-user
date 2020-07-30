package com.scootin.view.fragment.home

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentSplashBinding
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.home.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private var binding by autoCleared<FragmentSplashBinding>()
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)
        val videoUrl =
            Uri.parse("android.resource://" + requireContext().packageName.toString() + "/" + R.raw.video)
        binding.videoView.apply {
            setVideoURI(videoUrl)
            setOnCompletionListener { startNextActivity() }
            start()
        }
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