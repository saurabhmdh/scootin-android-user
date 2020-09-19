package com.scootin.view.fragment.delivery.sweet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentSweetsCategoryBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class SweetsCategoryFragment : Fragment(R.layout.fragment_sweets_category) {
    private var binding by autoCleared<FragmentSweetsCategoryBinding>()


    private val viewModel: HomeFragmentViewModel by viewModels()


    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSweetsCategoryBinding.bind(view)

        Timber.i("height =  ${binding.express.height} Width = ${binding.express.width}")
        updateListeners()
    }

    private fun updateListeners() {
        binding.sweets.setOnClickListener {
            findNavController().navigate(SweetsCategoryFragmentDirections.homeToSweets())
        }

        binding.bakery.setOnClickListener {
            findNavController().navigate(SweetsCategoryFragmentDirections.homeToCake())
        }

        binding.snacks.setOnClickListener {
            findNavController().navigate(SweetsCategoryFragmentDirections.homeToSnacks())
        }

        binding.cakenbouquet.setOnClickListener {
            findNavController().navigate(SweetsCategoryFragmentDirections.homeToCakenbouqet())
        }


    }
}