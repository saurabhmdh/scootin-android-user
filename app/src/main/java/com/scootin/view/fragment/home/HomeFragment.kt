package com.scootin.view.fragment.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
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
import com.scootin.util.fragment.autoCleared
import com.scootin.util.ui.UtilPermission
import com.scootin.view.activity.MainActivity
import com.scootin.view.adapter.DealAdapter
import com.scootin.view.adapter.DealFooterAdapter
import com.scootin.view.custom.CirclePagerIndicatorDecoration
import com.scootin.view.vo.ServiceArea
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment :  Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by viewModels()

    private var headerDealAdapter by autoCleared<DealAdapter>()
    private var footerDealAdapter by autoCleared<DealFooterAdapter>()

    private lateinit var homeCategoryList: List<HomeResponseCategory>
    private val timer = Timer()
    private val footerTimer = Timer()

    @Inject
    lateinit var appExecutors: AppExecutors


    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        updateListeners()
        checkForMap()
        setupRecycledView()
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
        serviceArea()
    }

    private fun serviceArea() {

        viewModel.getServiceArea().observe(viewLifecycleOwner, {cache->
            if (cache == null) {
                findNavController().navigate(HomeFragmentDirections.homeToServiceArea())
            } else {
                val listType = object : TypeToken<ServiceArea>() {}.type
                val serviceAreaInfo = Gson().fromJson<ServiceArea>(cache.value, listType)
                viewModel.updateServiceAreaDetail(serviceAreaInfo)
                binding.textBox.text = serviceAreaInfo?.name
            }
        })
        binding.textBox.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToServiceArea())
        }

    }

    private fun setupRecycledView() {
        headerDealAdapter = DealAdapter(appExecutors)
        footerDealAdapter = DealFooterAdapter(appExecutors)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.fragmentHomeContent.headerDeals)

        binding.fragmentHomeContent.headerDeals.apply {
            addItemDecoration(CirclePagerIndicatorDecoration())
            adapter = headerDealAdapter
        }
        binding.fragmentHomeContent.footerDeals.apply {
            addItemDecoration(CirclePagerIndicatorDecoration())
            adapter = footerDealAdapter
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
        binding.fragmentHomeContent.express.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToExpressDelivery())
        }

        binding.fragmentHomeContent.cityWideDelivery.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToCityDelivery())
        }

        binding.fragmentHomeContent.essentialsGrocery.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToEssential())
            } else {
                showDisabledText()
            }
        }

        binding.fragmentHomeContent.stationery.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToStationary())
            } else {
                showDisabledText()
            }
        }

        binding.fragmentHomeContent.sweetSnacks.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToSweets())
            } else {
                showDisabledText()
            }
        }


        binding.fragmentHomeContent.medicines.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToMedicines())
            } else {
                showDisabledText()
            }
        }

        binding.fragmentHomeContent.clothing.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToClothes())
            } else {
                showDisabledText()
            }
        }


        binding.fragmentHomeContent.vegetablesFruits.setOnClickListener {
            if (isActiveCategory(it.tag)) {
                viewModel.updateMainCategory(it.tag as String?)
                findNavController().navigate(HomeFragmentDirections.homeToVeg())
            } else {
                showDisabledText()
            }
        }

        viewModel.getCartCount(AppHeaders.userID).observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val result = it.body()?.toInt()
                val activity = activity as MainActivity?
                activity?.setupBadging(result.orZero())
            }
        }
        
        try {
            timer.scheduleAtFixedRate(getHeaderTask(), Date(),3000)
            footerTimer.scheduleAtFixedRate(getFooterTask(), Date(),4000)
        } catch (e: java.lang.Exception) {

        }


        viewModel.getAllDeals("HEADER").observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                headerDealAdapter.submitList(it.body())
            } else {
               Toast.makeText(requireContext(), "There is problem with data, Please check network connection", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getAllDeals("FOOTER").observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                footerDealAdapter.submitList(it.body())
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

    //This timer class will notify to UI that it should change the page
    fun getHeaderTask(): TimerTask{
        return object : TimerTask() {
            override fun run() {
                if(!isVisible) {
                    return
                }
                Timber.i("Timer task has been completed. Now we need to switch the image..")
                try {
                    scrollHeader()
                } catch (e: java.lang.Exception) {
                    Timber.i("Exception ${e.message}")
                }
            }
        }
    }

    fun getFooterTask(): TimerTask {
        return object : TimerTask() {
            override fun run() {
                if (!isVisible) {
                    return
                }
                Timber.i("Timer task has been completed. Now we need to switch the image..")
                try {
                    scrollFooter()
                } catch (e: java.lang.Exception) {
                    Timber.i("Exception ${e.message}")
                }
            }
        }
    }


    private fun scrollHeader() {
        val layoutman = binding.fragmentHomeContent.headerDeals.layoutManager as LinearLayoutManager
        val currentVisible = layoutman.findFirstVisibleItemPosition()
        val total = binding.fragmentHomeContent.headerDeals.adapter?.itemCount
        val position = if (currentVisible + 1 == total) 0 else currentVisible + 1
        Timber.i("Trying to move.. ${position} while total ${total} at present visible $currentVisible")
        layoutman.smoothScrollToPosition(binding.fragmentHomeContent.headerDeals, RecyclerView.State(), position)
    }

    private fun scrollFooter() {
        val layoutman = binding.fragmentHomeContent.footerDeals.layoutManager as LinearLayoutManager
        val currentVisible = layoutman.findFirstVisibleItemPosition()
        val total = binding.fragmentHomeContent.footerDeals.adapter?.itemCount ?: 0
        val position = if (currentVisible + 3 >= total) 0 else currentVisible + 3
        Timber.i("scrolling footer ${position} while total ${total} at present visible $currentVisible")
        layoutman.smoothScrollToPosition(binding.fragmentHomeContent.footerDeals, RecyclerView.State(), position)
    }

}