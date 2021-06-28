package com.scootin.view.fragment.account.address

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentAddNewAddressBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.extensions.orZero
import com.scootin.network.api.Resource
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddAddressRequest
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.State
import com.scootin.util.constants.Validation
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.BaseFragment
import com.scootin.view.fragment.delivery.medicines.MedicineDeliveryOrdersArgs
import com.scootin.viewmodel.account.AddressFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_new_address.view.*
import timber.log.Timber

@AndroidEntryPoint
class AddorModifyNewAddressFragment : BaseFragment(R.layout.fragment_add_new_address) {

    private var binding by autoCleared<FragmentAddNewAddressBinding>()
    private val viewModel: AddressFragmentViewModel by viewModels()

    private val args: AddorModifyNewAddressFragmentArgs by navArgs()

    private var savingOnGoing = false

    private val receivedAddress by lazy {
        args.addressDetail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddNewAddressBinding.bind(view)

        setupListener()

    }

    private fun setupListener() {
        Timber.i("Setting up listeners..")

        viewModel.stateInfo.observe(viewLifecycleOwner) {
            getStates(it)
        }

        binding.save.setOnClickListener {
            saveAddress()
        }
       // binding.back.setOnClickListener { findNavController().popBackStack() }

        //Prefilled
        binding.mobileEditText.setText(AppHeaders.userMobileNumber)

        receivedAddress?.let {
            //Update address with previous data
            preFilledAddressData(it)
        }
    }

    private fun preFilledAddressData(addressDetails: AddressDetails) {
        binding.enteredAddressEditText.setText(addressDetails.addressLine1)
        binding.enteredAreaEditText.setText(addressDetails.addressLine2)
        binding.enteredCityEditText.setText(addressDetails.city)
        binding.enteredPinEditText.setText(addressDetails.pincode)
        binding.addressId.setText(addressDetails.id.toString())
        when (addressDetails.addressType) {
            "HOME" -> {binding.radioGroup.home_radio_button.isChecked = true}
            "OFFICE" -> {binding.radioGroup.office_radio_button.isChecked = true}
            "OTHERS" -> {binding.radioGroup.other_radio_button.isChecked = true}
        }
        binding.setAsDefault.isChecked = addressDetails.hasDefault

        binding.emailEditText.setText(addressDetails.email ?: "")
        binding.mobileEditText.setText(addressDetails.mobileNumber)
        binding.nameEditText.setText(addressDetails.name)
    }


    private fun getStates(it: Resource<List<State>>) {
        when (it.status) {
            Status.SUCCESS -> {
                val list: List<State> = it.data ?: emptyList()
                binding.stateAdapter.adapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)

                var selection = 0
                for (i in 0 until binding.stateAdapter.adapter?.count.orZero()) {
                    val state = binding.stateAdapter.adapter.getItem(i) as State?
                    if (receivedAddress?.stateDetails?.name == state?.name) {
                        selection = i
                        break
                    } else if (state?.name == "UTTAR PRADESH") {
                        selection = i
                        break
                    }
                }
                binding.stateAdapter.setSelection(selection)
            }
            Status.LOADING -> {

            }
            Status.ERROR -> {
                Toast.makeText(
                    requireContext(),
                    "There is error while selecting states",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveAddress() {
        if (savingOnGoing) {
            return
        }

        val name = binding.nameEditText.text?.toString()
        if (name.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please provide a name", Toast.LENGTH_SHORT).show()
            return
        }

        val mobileNumber = binding.mobileEditText.text?.toString()
        if (mobileNumber.isNullOrEmpty() || Validation.REGEX_VALID_MOBILE_NUMBER.matcher(mobileNumber).matches().not()) {
            Toast.makeText(context, R.string.error_message_invalid_mobile, Toast.LENGTH_SHORT).show()
            return
        }

        val email = binding.emailEditText.text?.toString()
        if (email.isNullOrEmpty().not()  && Validation.REGEX_VALID_EMAIL.matcher(email).matches().not()) {
            Toast.makeText(context, R.string.error_message_invalid_email, Toast.LENGTH_SHORT).show()
            return
        }


        val address = binding.enteredAddressEditText.text?.toString()
        if (address.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please enter address", Toast.LENGTH_SHORT).show()
            return
        }

        val area = binding.enteredAreaEditText.text?.toString()
        if (area.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please enter area", Toast.LENGTH_SHORT).show()
            return
        }

        val city = binding.enteredCityEditText.text?.toString()
        if (city.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please enter city", Toast.LENGTH_SHORT).show()
            return
        }

        val pin = binding.enteredPinEditText.text?.toString()
        if (pin.isNullOrEmpty() || pin.length < 6) {
            Toast.makeText(requireContext(), "Please enter valid pin", Toast.LENGTH_SHORT).show()
            return
        }

        val state = binding.stateAdapter.selectedItem as State?

        if (state == null) {
            Toast.makeText(requireContext(), "Please select valid state", Toast.LENGTH_SHORT).show()
            return
        }

        val id = binding.addressId.text?.toString()?.toLongOrNull() ?: -1

        val addressType = when (binding.radioGroup.getCheckedRadioButtonPosition()) {
            0 -> {
                "HOME"
            }
            1 -> {
                "OFFICE"
            }
            2 -> {
                "OTHERS"
            }
            else -> {
                Toast.makeText(
                    requireContext(),
                    "Please select valid address type",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }
        val hasDefault = binding.setAsDefault.isChecked

        showLoading()
        savingOnGoing = true
        viewModel.saveAddress(
            AddAddressRequest(
                name,
                email,
                mobileNumber,
                addressType,
                address,
                area,
                city,
                hasDefault,
                pin,
                state.id,
                id
            )
        ).observe(viewLifecycleOwner) {
            savingOnGoing = false
            dismissLoading()
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Address added successfully", Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "There is error while saving address",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}