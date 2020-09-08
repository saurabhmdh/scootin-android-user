package com.scootin.view.fragment.account.Address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.scootin.R
import com.scootin.databinding.FragmentAccountBinding
import com.scootin.databinding.FragmentAddNewAddressBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.AccountFragmentViewModel
import com.scootin.viewmodel.account.AddressFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddressFragment : Fragment(R.layout.fragment_add_new_address) {

    private var binding by autoCleared<FragmentAddNewAddressBinding>()
    private val viewModel: AddressFragmentViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddNewAddressBinding.bind(view)

    }
}