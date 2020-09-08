package com.scootin.view.fragment.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentAccountBinding
import com.scootin.databinding.FragmentHomeBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.home.HomeFragmentDirections
import com.scootin.viewmodel.account.AccountFragmentViewModel
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment: Fragment(R.layout.fragment_account)  {

    private var binding by autoCleared<FragmentAccountBinding>()
    private val viewModel: AccountFragmentViewModel by viewModels()


    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountBinding.bind(view)

        updateListeners()

    }

    private fun updateListeners() {

        binding.addNewAddressImageButton.setOnClickListener {
            findNavController().navigate(R.id.add_new_address_image_button)
        }

        binding.myOrdersImageButton.setOnClickListener{
            findNavController().navigate(R.id.my_orders_image_button)
        }

        binding.savedCartImageButton.setOnClickListener{
            findNavController().navigate(R.id.saved_cart_image_button)
        }

        binding.supportFeedbackImageButton.setOnClickListener{
            findNavController().navigate(R.id.support_feedback_image_button)
        }

    }
}
