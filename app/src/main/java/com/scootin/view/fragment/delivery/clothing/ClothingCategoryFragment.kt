package com.scootin.view.fragment.delivery.clothing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentClothingPageBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class ClothingCategoryFragment : Fragment(R.layout.fragment_clothing_page) {
    private var binding by autoCleared<FragmentClothingPageBinding>()


    private val viewModel: HomeFragmentViewModel by viewModels()
    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClothingPageBinding.bind(view)
        updateListeners()
    }

    private fun updateListeners() {
        binding.menInnerWear.setOnClickListener {
            viewModel.updateSubCategory(it.tag as String?)
            findNavController().navigate(ClothingCategoryFragmentDirections.homeToMeninnerwear())
        }

        binding.womenInnerWear.setOnClickListener {
            viewModel.updateSubCategory(it.tag as String?)
            findNavController().navigate(ClothingCategoryFragmentDirections.homeToWomeninnerwear())
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }

    }
}