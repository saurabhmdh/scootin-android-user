package com.scootin.view.fragment.delivery.essential

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentEssentialCategoryBinding
import com.scootin.databinding.FragmentGroceryDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.sweets.SweetsItem
import com.scootin.network.response.sweets.SweetsStore
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.EssentialGroceryAddAdapter
import com.scootin.view.adapter.EssentialGroceryStoreAdapter
import com.scootin.view.fragment.dialogs.EssentialCategoryDialogFragment
import javax.inject.Inject

class EssentialCategoryFragment : Fragment(R.layout.fragment_essential_category) {
    private var binding by autoCleared<FragmentEssentialCategoryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEssentialCategoryBinding.bind(view)
        binding.btnDone.setOnClickListener {
            findNavController().navigate(EssentialCategoryFragmentDirections.essentialCategoryToGrocerydelivery())
        }
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            when (optionId) {
                R.id.materialRadioButton -> {
                    binding.btnDone.setOnClickListener {
                        findNavController().navigate(EssentialCategoryFragmentDirections.essentialCategoryToGrocerydelivery())
                    }
                }
                R.id.materialRadioButton2 -> {
                    binding.btnDone.setOnClickListener {
                        findNavController().navigate(EssentialCategoryFragmentDirections.essentialCategoryToHandwritten())
                    }
                }
            }
        }
    }




}