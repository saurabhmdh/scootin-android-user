package com.scootin.view.fragment.account

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentAccountBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.response.Address
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.AccountFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private var binding by autoCleared<FragmentAccountBinding>()
    private val viewModel: AccountFragmentViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    var addressType = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountBinding.bind(view)

        updateListeners()

        initObservers(view)
    }

    private fun initObservers(view: View) {
        viewModel.updateDefaultAddress("")
        viewModel.addNewAddressLiveData.observe(viewLifecycleOwner, Observer {

        })

        viewModel.updateDefaultAddressLiveData.observe(viewLifecycleOwner, Observer {

        })

        viewModel.getAllAddressLiveData.observe(viewLifecycleOwner, Observer {

        })

        binding.addressFragment.setOnClickListener {
            if (binding.newAddressLayout.visibility == View.VISIBLE) {
                binding.newAddressLayout.updateVisibility(false)
            } else {
                binding.newAddressLayout.updateVisibility(true)
            }
        }
    }

    private fun updateListeners() {

        binding.radioGroup.setOnCheckedChangeListener { group, checkId ->
            val checkedRadioButton = group.findViewById<RadioButton>(checkId)
            val isChecked = checkedRadioButton.isChecked()
            if (isChecked) {
                Timber.i("Checked:" + checkedRadioButton.getText())
                addressType = checkedRadioButton.getText().toString()
            }
        }

        binding.addressFragment.setOnClickListener {

            findNavController().navigate(AccountFragmentDirections.accountToAddressFragment())
        }

        binding.ordersFragment.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.accountToOrdersFragment())
        }

        binding.cardsFragment.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.accountToCartFragment())
        }

        binding.supportFragment.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.accountToSupportFragment())
        }

        binding.update1.setOnClickListener {
            val address = Address(
                binding.enteredAddressEditText1.text.toString(),
                "type",
                binding.enteredCityEditText1.text.toString(),
                binding.enteredPinEditText1.text.toString(),
                binding.enteredStateEditText1.text.toString()
            )
            viewModel.addNewAddress(address)
        }

    }
}
