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
import timber.log.Timber
import javax.inject.Inject

class ClothingCategoryFragment : Fragment(R.layout.fragment_clothing_page) {
    private var binding by autoCleared<FragmentClothingPageBinding>()


    private val viewModel: HomeFragmentViewModel by viewModels()
    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClothingPageBinding.bind(view)

        Timber.i("height =  ${binding.express.height} Width = ${binding.express.width}")
        updateListeners()
    }

    private fun updateListeners() {
        binding.menInnerWear.setOnClickListener {
            findNavController().navigate(ClothingCategoryFragmentDirections.homeToMeninnerwear())
        }

        binding.womenInnerWear.setOnClickListener {
            findNavController().navigate(ClothingCategoryFragmentDirections.homeToWomeninnerwear())
        }



    }
}