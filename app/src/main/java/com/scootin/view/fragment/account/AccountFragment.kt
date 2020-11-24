package com.scootin.view.fragment.account

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentAccountBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.response.State
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.AccountFragmentViewModel
import com.scootin.viewmodel.account.AddressFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private var binding by autoCleared<FragmentAccountBinding>()
    private val viewModel: AccountFragmentViewModel by viewModels()
    private val addressViewModel: AddressFragmentViewModel by viewModels()
    private var stateList = ArrayList<State>()

    @Inject
    lateinit var appExecutors: AppExecutors
    var addressType = "Home"

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

        /* addressViewModel.state(1)
         addressViewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
             if (it.isSuccessful) {
                 stateList = it.body() as ArrayList<State>
             }
         })*/
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
            Timber.i("addressTag = ${binding.enteredStateEditText1.tag}")
            val address = Address(
                binding.enteredAddressEditText1.text.toString(),
                addressType,
                binding.enteredCityEditText1.text.toString(),
                binding.enteredPinEditText1.text.toString(),
                binding.enteredStateEditText1.tag.toString()
            )
            viewModel.addNewAddress(address)
        }

        binding.enteredStateEditText1.setOnClickListener {
            val stateMap = stateList.map { it.name }
            showStateList(stateMap)
        }
    }

    private fun showStateList(
        stateMap: List<String>
    ) {
        Timber.i("showPopupList clicked")
        val listPopupWindow = ListPopupWindow(requireContext())
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            stateMap
        )
        listPopupWindow.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                Timber.i("position clicked = ${stateMap.get(position)}  id = ${stateList.get(position).id}")
                binding.enteredStateEditText1.setText(stateMap.get(position))
                binding.enteredStateEditText1.setTag(stateList.get(position).id)
                listPopupWindow.dismiss()
            }
            anchorView = view
            show()
        }
    }

}
