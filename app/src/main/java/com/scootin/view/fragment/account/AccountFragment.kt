package com.scootin.view.fragment.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentAccountBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.AccountFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
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

        initObservers(view)
    }

    private fun initObservers(view: View) {
        viewModel.getAllAddress(1)
        viewModel.addNewAddress(null)
        viewModel.updateDefaultAddress("")
        viewModel.addNewAddressLiveData.observe(viewLifecycleOwner, Observer {

        })

        viewModel.updateDefaultAddressLiveData.observe(viewLifecycleOwner, Observer {

        })

        viewModel.getAllAddressLiveData.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun updateListeners() {

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

        binding.update.setOnClickListener {
            // TODO update Address
//            val address = CartListResponseItem.Address()
            viewModel.updateDefaultAddress("")
        }

    }
}
