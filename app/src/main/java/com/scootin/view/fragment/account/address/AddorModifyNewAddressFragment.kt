package com.scootin.view.fragment.account.address

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentAddNewAddressBinding
import com.scootin.util.fragment.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddorModifyNewAddressFragment : Fragment(R.layout.fragment_add_new_address) {

    private var binding by autoCleared<FragmentAddNewAddressBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddNewAddressBinding.bind(view)

        setupListener()

    }

    private fun setupListener() {
       Timber.i("Setting up listeners..")
    }

}