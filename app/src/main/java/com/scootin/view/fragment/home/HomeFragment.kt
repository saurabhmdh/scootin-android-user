package com.scootin.view.fragment.home

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.scootin.databinding.FragmentHomeBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.home.HomeFragmentViewModel
import timber.log.Timber
import javax.inject.Inject

import androidx.navigation.fragment.findNavController

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.iid.FirebaseInstanceId
import com.scootin.R
import com.scootin.network.api.Status
import com.scootin.util.constants.AppConstants
import com.scootin.util.ui.UtilPermission

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment :  Fragment(R.layout.fragment_home) {
    private var binding by autoCleared<FragmentHomeBinding>()
    private val viewModel: HomeFragmentViewModel by viewModels()

    //This code is for Rider..
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        Timber.i("height =  ${binding.express.height} Width = ${binding.express.width}")
        updateListeners()

        //Find user location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkForMap()

        //Let me try firebase integration..
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if(!it.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = it.result?.token
            Timber.i("Saurabh : Device token $token")
            //Update FCM ID for user & read user from user info.
        }
        doNetworkCall()
    }


    private fun doNetworkCall() {
        viewModel.getHomeCategory().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {

                }
                Status.SUCCESS -> {
                    Timber.i("Samridhi ${it.data}")
                }
                Status.LOADING -> {
                }
            }
        })
    }

    private fun updateListeners() {
        binding.express.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToExpressDelivery())
        }

        binding.cityWideDelivery.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToCityDelivery())
        }

        binding.essentialsGrocery.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToEssential())
        }

        binding.stationery.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToStationary())
        }

        binding.sweetSnacks.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToSweets())
        }


        binding.medicines.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToMedicines())
        }

        binding.clothing.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToClothes())
        }


        binding.vegetablesFruits.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToVeg())
        }


    }


    private fun setupLocationUpdateListener() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    //https://developers.google.com/maps/documentation/geocoding/overview?csw=1#ReverseGeocoding
                    Timber.i("fusedLocationClient lastLocation: = ${location}")
                    location?.let {
                        Timber.i("get location: ${location.latitude}, ${location.longitude}")
                    }
                }
                .addOnFailureListener {
                    Timber.i("update location fail: $it")
                }
        } catch (e: SecurityException) {
            Timber.i("We don't have permission of map: ${e.message}")
            UtilPermission.requestMapPermission(this)
        }
    }


    private fun checkForMap() {
        if (!UtilPermission.hasMapPermission(requireContext())) {
            UtilPermission.requestMapPermission(this)
        } else {
            setupLocationUpdateListener()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            AppConstants.RC_LOCATION_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setupLocationUpdateListener()
                }
            }
            else -> {}
        }
    }
}