package com.scootin.view.fragment.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentSplashBinding
import com.scootin.util.fragment.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var binding by autoCleared<FragmentSplashBinding>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("Deeplink path = ${activity?.intent?.data?.path} last path segment = ${activity?.intent?.data?.lastPathSegment} query = ${activity?.intent?.data?.query}")

        Handler().postDelayed({
            gotoNextFragment()
        }, 3000)


    }

    private fun gotoNextFragment() {
        findNavController().navigate(
            R.id.nav_home,
            null,
            NavOptions.Builder().setPopUpTo(R.id.splash_fragment, true).build()
        )
    }

}