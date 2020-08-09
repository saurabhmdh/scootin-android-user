package com.scootin.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.scootin.databinding.FragmentHomeBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.home.HomeFragmentViewModel
import timber.log.Timber
import javax.inject.Inject
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.scootin.R
import com.scootin.view.adapter.home.TempleListAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment :  Fragment(R.layout.fragment_home) {
    private var binding by autoCleared<FragmentHomeBinding>()


    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var adapter: TempleListAdapter

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        Timber.i("height =  ${binding.express.height} Width = ${binding.express.width}")
        updateListeners()
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
//            findNavController().navigate(HomeFragmentDirections.homeToSnacks())
        }


        binding.vegetablesFruits.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToVeg())
        }


    }

//    private fun initUI() {
//        adapter = TempleListAdapter(appExecutors)
//        binding.suggestionList.adapter = adapter
//        binding.suggestionList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
//    }

//
//    private fun observeDataChange() {
//        viewModel.getAllTemples().observe(viewLifecycleOwner) {
//            Timber.i("data from network  ${it.data}")
//            adapter.submitList(it.data)
//        }
//    }
}