package com.scootin.view.fragment.delivery.essential

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentEssentialCategoryBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import javax.inject.Inject

class EssentialCategoryFragment : Fragment(R.layout.fragment_essential_category) {
    private var binding by autoCleared<FragmentEssentialCategoryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEssentialCategoryBinding.bind(view)
        updateListeners()
    }

    private fun updateListeners() {
        binding.btnDone.setOnClickListener {
            when(binding.radioGroup.getCheckedRadioButtonPosition()) {
                0 -> {
                    findNavController().navigate(EssentialCategoryFragmentDirections.essentialCategoryToGrocerydelivery())
                }
                1 -> {
                    findNavController().navigate(EssentialCategoryFragmentDirections.essentialCategoryToHandwritten())
                }
            }
        }
        binding.back.setOnClickListener { findNavController().popBackStack() }
    }
}