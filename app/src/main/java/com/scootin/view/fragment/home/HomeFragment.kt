package com.scootin.view.fragment.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.scootin.databinding.FragmentHomeBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.home.HomeFragmentViewModel
import timber.log.Timber
import javax.inject.Inject
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.iid.FirebaseInstanceId
import com.scootin.R
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.util.constants.AppConstants
import com.scootin.util.ui.UtilPermission

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment :  Fragment(R.layout.fragment_home) {
    private var binding by autoCleared<FragmentHomeBinding>()
    private val viewModel: HomeFragmentViewModel by viewModels()


    private lateinit var homeCategoryList: List<HomeResponseCategory>

    @Inject
    lateinit var appExecutors: AppExecutors

    private val AUTOCOMPLETE_REQUEST_CODE = 1

    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        Timber.i("height =  ${binding.express.height} Width = ${binding.express.width}")
        updateListeners()

        checkForMap()

        //Let me try firebase integration..
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if(!it.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = it.result?.token
            Timber.i("Saurabh : Device token $token for user ${AppHeaders.userID}")
            viewModel.updateFCMID(token)
        }
        doNetworkCall()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.i("onActivityResult $requestCode $resultCode -> $data")

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        handlePlaceSuccessResponse(place)
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Timber.i(status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun doNetworkCall() {
        viewModel.getHomeCategory().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {
                }
                Status.SUCCESS -> {
                    Timber.i("Category Response ${it.data}")
                    it.data?.let {list->
                        homeCategoryList = list
                    }
                }
                Status.LOADING -> {
                }
            }
        })

        viewModel.serviceArea.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Congratulation!! We are serving in area = " +it.name, Toast.LENGTH_LONG).show()
        })

        viewModel.serviceAreaError.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Sorry!! Our services are not allowed in this area..\n Please change the location..", Toast.LENGTH_LONG).show()
        })
    }


    private fun isActiveCategory(tag: Any?): Boolean {
        if (!::homeCategoryList.isInitialized) {
            return true
        }
        val tagID = tag as String?
        val data = homeCategoryList.find { it.id == tagID?.toInt()}
        return data?.active == true && !data.deleted
    }

    private fun showDisabledText() {
        Toast.makeText(context, R.string.category_disabled ,Toast.LENGTH_SHORT).show()
    }

    private fun updateListeners() {
        binding.express.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToExpressDelivery())
        }

        binding.cityWideDelivery.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToCityDelivery())
        }

        binding.essentialsGrocery.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToEssential())
            } else {
                showDisabledText()
            }
        }

        binding.stationery.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToStationary())
            } else {
                showDisabledText()
            }
        }

        binding.sweetSnacks.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToSweets())
            } else {
                showDisabledText()
            }
        }


        binding.medicines.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToMedicines())
            } else {
                showDisabledText()
            }
        }

        binding.clothing.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToClothes())
            } else {
                showDisabledText()
            }
        }


        binding.vegetablesFruits.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToVeg())
            } else {
                showDisabledText()
            }
        }


        binding.userLocation.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        viewModel.presentLocation.observe(viewLifecycleOwner, {
            Timber.i("We have got below location $it")
            if(it != null) {
                updateAddress(it.address)
            }
        })

    }

    private fun handlePlaceSuccessResponse(place: Place?) {
        place?.let {
            Timber.i("Place: ${it.name}, ${it.id} ")
            Timber.i("Place: ${it.latLng?.latitude}, ${it.latLng?.longitude}, ${it.address} ")
            viewModel.updateLocation(place)
            updateAddress(it.address)
            updateServiceArea(it.latLng)
        }
    }

    private fun updateServiceArea(location: LatLng?) {
        location?.let {
            Timber.i("get location: ${it.latitude}, ${it.longitude}")
            viewModel.findServiceArea(it.latitude, it.longitude)
        }

    }

    private fun updateAddress(address : String?) {
        address?.let {
            binding.userLocation.text = it
        }
    }

    private fun checkForMap() {
        if (!UtilPermission.hasMapPermission(requireContext())) {
            UtilPermission.requestMapPermission(this)
        } else {
            getAddressFromPlaces()
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
                    getAddressFromPlaces()
                }
            }
            else -> {}
        }
    }

    private fun getAddressFromPlaces() {
        if (viewModel.presentLocation.value != null) {
            Timber.i("Already has location..")
            return
        }
        Timber.i("We need to find location of user.")
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(fields)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            val placesClient = Places.createClient(requireContext())
            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result
                    val place = response?.placeLikelihoods?.first()?.place
                    place?.let {
                        handlePlaceSuccessResponse(place)
                    }
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Timber.e( "Place not found: ${exception.statusCode}")
                    }
                }
            }
        } else {
            UtilPermission.requestMapPermission(this)
        }
    }
}