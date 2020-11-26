package com.scootin.view.fragment.account.address

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentAddNewAddressBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.extensions.orZero
import com.scootin.network.api.Status
import com.scootin.network.request.AddAddressRequest
import com.scootin.network.response.State
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.AddressFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddorModifyNewAddressFragment : Fragment(R.layout.fragment_add_new_address) {

    private var binding by autoCleared<FragmentAddNewAddressBinding>()
    private val viewModel: AddressFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddNewAddressBinding.bind(view)

        setupListener()

    }

    private fun setupListener() {
        Timber.i("Setting up listeners..")

        viewModel.stateInfo.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    val list: List<State> = it.data ?: emptyList()
                    binding.stateAdapter.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)

                    var selection = 0
                    for (i in 0 until binding.stateAdapter.adapter?.count.orZero()) {
                        val state = binding.stateAdapter.adapter.getItem(i) as State?
                        if (state?.name == "UTTAR PRADESH") {
                            selection = i
                        }
                    }
                    binding.stateAdapter.setSelection(selection)
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "There is error while selecting states", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.save.setOnClickListener {

            val address = binding.enteredAddressEditText.text?.toString()
            if (address.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please enter address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val area = binding.enteredAreaEditText.text?.toString()
            if (area.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please enter area", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val city = binding.enteredCityEditText.text?.toString()
            if (city.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please enter city", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val pin = binding.enteredPinEditText.text?.toString()
            if (pin.isNullOrEmpty() || pin.length < 6) {
                Toast.makeText(requireContext(), "Please enter valid pin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val state = binding.stateAdapter.selectedItem as State?

            if (state == null) {
                Toast.makeText(requireContext(), "Please select valid state", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val addressType = when(binding.radioGroup.getCheckedRadioButtonPosition()) {
                0-> {"HOME"}
                1 -> {"OFFICE"}
                2 -> {"OTHERS"}
                else -> {
                    Toast.makeText(requireContext(), "Please select valid address type", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            val hasDefault = binding.setAsDefault.isChecked
            viewModel.saveAddress(AddAddressRequest(addressType, address, city, hasDefault, pin, state.id)).observe(viewLifecycleOwner) {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Address added successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "There is error while saving address", Toast.LENGTH_SHORT).show()
                }
            }

        }
        binding.back.setOnClickListener { findNavController().popBackStack() }
    }

}