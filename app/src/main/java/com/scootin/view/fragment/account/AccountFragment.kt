package com.scootin.view.fragment.account

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.scootin.R
import com.scootin.databinding.FragmentAccountBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.CancelOrderRequest
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.State
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.SplashActivity
import com.scootin.view.vo.AddressVo
import com.scootin.viewmodel.account.AccountFragmentViewModel
import com.scootin.viewmodel.account.AddressFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private var binding by autoCleared<FragmentAccountBinding>()
    private val viewModel: AccountFragmentViewModel by viewModels()
    private val addressViewModel:AddressFragmentViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountBinding.bind(view)

        updateListeners()

        initObservers()

        handleLogOut()


    }

    private fun initObservers() {
        addressViewModel.loadAddress()
        addressViewModel.getAddressLiveData().observe(viewLifecycleOwner) {
            if (it.isSuccessful) {

                it.body()?.forEach { item ->
                    if(item.hasDefault){
                        binding.nameEditText.setText(item.name)
                        binding.emailEditText.setText(item.email)
                        binding.mobileEditText.setText(AppHeaders.userMobileNumber)
                    }
                }

            } else {
                Toast.makeText(requireContext(), "There is some error while getting address", Toast.LENGTH_SHORT).show()
            }
        }
    }

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

        binding.logoutBtn.setOnClickListener {
            val alertDialog = context?.let { it1 -> MaterialAlertDialogBuilder(it1) }

            alertDialog?.setMessage(R.string.logoutDialogMessage)
            alertDialog?.setIcon(android.R.drawable.ic_dialog_alert)


            alertDialog?.setPositiveButton("Yes") { dialogInterface, which ->
                viewModel.doLogout()
            }

            alertDialog?.setNegativeButton("No") { dialogInterface, which ->

            }



            alertDialog?.setCancelable(false)

            alertDialog?.show()

        }

    }
    private fun handleLogOut() {
        viewModel.logoutComplete.observe(viewLifecycleOwner) {
            gotoStart()
        }
    }

    private fun gotoStart() {
        startActivity(Intent(requireContext(), SplashActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }

}
