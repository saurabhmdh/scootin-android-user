package com.scootin.view.fragment.delivery.city

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.ActivityMainBinding.bind
import com.scootin.databinding.FragmentCitywideCategoryBinding
import com.scootin.databinding.FragmentEssentialCategoryBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.delivery.essential.EssentialCategoryFragmentDirections
import javax.inject.Inject

class FragmentCityWideCategory: Fragment(R.layout.fragment_citywide_category) {
    private var binding by autoCleared<FragmentCitywideCategoryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCitywideCategoryBinding.bind(view)
            binding.btnDone.setOnClickListener {
                findNavController().navigate(FragmentCityWideCategoryDirections.citycategoryToCitydelivery())
            }
        }
}