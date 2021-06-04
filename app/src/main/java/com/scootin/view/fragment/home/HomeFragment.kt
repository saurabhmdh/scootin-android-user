package com.scootin.view.fragment.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.model.Place
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

        Timber.i("height =  ${binding.express.height} Width = ${binding.express.width}")
        updateListeners()


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

        viewModel.getServiceArea().observe(viewLifecycleOwner, {cache->
            if (cache == null) {
                findNavController().navigate(HomeFragmentDirections.homeToServiceArea())
            } else {
                val listType = object : TypeToken<ServiceArea>() {}.type
                val serviceAreaInfo = Gson().fromJson<ServiceArea>(cache.value, listType)
                binding.userLocation.text = serviceAreaInfo?.name
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
            findNavController().navigate(HomeFragmentDirections.homeToServiceArea())
        }


        viewModel.getCartCount(AppHeaders.userID).observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val result = it.body()?.toInt()
                val activity = activity as MainActivity?
                activity?.setupBadging(result.orZero())
            }
        }
    }

}