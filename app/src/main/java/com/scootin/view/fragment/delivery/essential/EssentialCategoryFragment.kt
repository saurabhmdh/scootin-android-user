package com.scootin.view.fragment.delivery.essential

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentEssentialCategoryBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.network.AppExecutors
import com.scootin.network.response.AddressDetails
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.AddressFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class EssentialCategoryFragment : Fragment(R.layout.fragment_essential_category) {
    private var binding by autoCleared<FragmentEssentialCategoryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private val addressViewModel: AddressFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEssentialCategoryBinding.bind(view)
        updateListeners()
    }

    private fun updateListeners() {
        binding.btnDone.setOnClickListener {
            //If there is no delivery slot it should make error
//            if (binding.deliverySlot.selectedItem?.toString().isNullOrEmpty()) {
//                Toast.makeText(requireContext(), R.string.error_no_internet, Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
            when (binding.radioGroup.getCheckedRadioButtonPosition()) {
                0 -> {
                    findNavController().navigate(EssentialCategoryFragmentDirections.essentialCategoryToGrocerydelivery())
                }
                1 -> {
                    findNavController().navigate(EssentialCategoryFragmentDirections.essentialCategoryToHandwritten())
                }
            }
        }
        //binding.back.setOnClickListener { findNavController().popBackStack() }

//        addressViewModel.deliverySlot.observe(viewLifecycleOwner) {
//            if (it.isSuccessful) {
//                val list: List<String> = it.body() ?: emptyList()
//                binding.deliverySlot.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)
//            } else {
//                //Let me try again 3 times...
//                Toast.makeText(
//                    requireContext(),
//                    R.string.error_no_internet,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
    }
}