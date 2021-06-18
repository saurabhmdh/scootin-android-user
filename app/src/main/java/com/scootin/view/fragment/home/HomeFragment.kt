package com.scootin.view.fragment.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.R
import com.scootin.databinding.FragmentHomeBinding
import com.scootin.extensions.orZero
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.util.constants.AppConstants
import com.scootin.util.ui.UtilPermission
import com.scootin.view.activity.MainActivity
import com.scootin.view.vo.ServiceArea
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment :  Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by viewModels()

    private lateinit var homeCategoryList: List<HomeResponseCategory>

    @Inject
    lateinit var appExecutors: AppExecutors


    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        setHasOptionsMenu(true)
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
    override fun onDestroyView() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val textBox = menu.findItem(R.id.action_text).actionView.findViewById<TextView>(
            R.id.text_box
        )
        viewModel.getServiceArea().observe(viewLifecycleOwner, {cache->
            if (cache == null) {
                findNavController().navigate(HomeFragmentDirections.homeToServiceArea())
            } else {
                val listType = object : TypeToken<ServiceArea>() {}.type
                val serviceAreaInfo = Gson().fromJson<ServiceArea>(cache.value, listType)
                viewModel.updateServiceAreaDetail(serviceAreaInfo)
                textBox.text = serviceAreaInfo?.name
            }
        })
        textBox.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToServiceArea())
        }

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


    }


//    private fun isActiveCategory(tag: Any?): Boolean {
//        if (!::homeCategoryList.isInitialized) {
//            return true
//        }
//        val tagID = tag as String?
//        val data = homeCategoryList.find { it.id == tagID?.toInt()}
//        return data?.active == true && !data.deleted
//    }

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
            findNavController().navigate(HomeFragmentDirections.homeToEssential())
//            if (isActiveCategory(it.tag)) {
//                viewModel.updateMainCategory(it.tag as String?)
//                findNavController().navigate(HomeFragmentDirections.homeToEssential())
//            } else {
//                showDisabledText()
//            }
        }

        binding.stationery.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToStationary())
//            if (isActiveCategory(it.tag)) {
//                viewModel.updateMainCategory(it.tag as String?)
//                findNavController().navigate(HomeFragmentDirections.homeToStationary())
//            } else {
//                showDisabledText()
//            }
        }

        binding.sweetSnacks.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToSweets())
//            if (isActiveCategory(it.tag)) {
//                viewModel.updateMainCategory(it.tag as String?)
//                findNavController().navigate(HomeFragmentDirections.homeToSweets())
//            } else {
//                showDisabledText()
//            }
        }


        binding.medicines.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToMedicines())
//            if (isActiveCategory(it.tag)) {
//                viewModel.updateMainCategory(it.tag as String?)
//                findNavController().navigate(HomeFragmentDirections.homeToMedicines())
//            } else {
//                showDisabledText()
//            }
        }

        binding.clothing.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToClothes())
//            if (isActiveCategory(it.tag)) {
//                viewModel.updateMainCategory(it.tag as String?)
//                findNavController().navigate(HomeFragmentDirections.homeToClothes())
//            } else {
//                showDisabledText()
//            }
        }


        binding.vegetablesFruits.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToVeg())
//            if (isActiveCategory(it.tag)) {
//                viewModel.updateMainCategory(it.tag as String?)
//                findNavController().navigate(HomeFragmentDirections.homeToVeg())
//            } else {
//                showDisabledText()
//            }
        }





        viewModel.getCartCount(AppHeaders.userID).observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val result = it.body()?.toInt()
                val activity = activity as MainActivity?
                activity?.setupBadging(result.orZero())
            }
        }
    }


    private fun handlePlaceSuccessResponse(place: Place?, adminArea: String? = null) {
        place?.let {
            Timber.i("Place: ${it.name}, ${it.id} ")
            val address = adminArea ?: it.address
            Timber.i("Place: ${adminArea},  ${it.address} final address $address")
            Timber.i("Place: ${it.latLng?.latitude}, ${it.latLng?.longitude}")
            viewModel.updateLocation(place, adminArea)
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
                        //Lets try geo coder
                        try {
                            val listOfAddress = Geocoder(context).getFromLocation(it.latLng!!.latitude, it.latLng!!.longitude, 1)
                            val address = listOfAddress.firstOrNull()
                            handlePlaceSuccessResponse(place, address?.subAdminArea)
                        } catch (e: Exception) {
                            //Do nothing...
                        }
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