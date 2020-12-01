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
import com.scootin.network.response.AddressDetails
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

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountBinding.bind(view)

        updateListeners()

        initObservers()
    }

    private fun initObservers() {}

    private fun updateListeners() {
        binding.addressFragment.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.accountToAddressFragment())
        }

        binding.ordersFragment.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.accountToOrdersFragment())
        }

        binding.supportFragment.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.accountToSupportFragment())
        }

    }

}
