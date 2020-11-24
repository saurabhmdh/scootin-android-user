package com.scootin.view.fragment.delivery.essential

import android.os.Bundle
import android.view.View
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
        addressViewModel.addressLiveData.observe(viewLifecycleOwner, Observer {
            Timber.i("address = ${it.body()}")
            val response = it.body()
            showAddressList(response)
        })
    }

    private fun updateListeners() {
        binding.btnDone.setOnClickListener {
            when (binding.radioGroup.getCheckedRadioButtonPosition()) {
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


    private fun showAddressList(
        addressList: List<AddressDetails>?
    ) {
        Timber.i("showPopupList clicked")
        val listPopupWindow = ListPopupWindow(requireContext())
        val adapter = AddressArrayAdapter(
            requireContext(),
            addressList
        )
        listPopupWindow.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                when (addressList?.get(position)) {

                }
                listPopupWindow.dismiss()
            }
            anchorView = view
            show()
        }
    }
}