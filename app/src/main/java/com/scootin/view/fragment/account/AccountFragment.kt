package com.scootin.view.fragment.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.scootin.BuildConfig
import com.scootin.R
import com.scootin.databinding.FragmentAccountBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.SplashActivity
import com.scootin.viewmodel.account.AccountFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
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

        handleLogOut()


    }

    private fun initObservers() {

        binding.mobileEditText.text = AppHeaders.userMobileNumber

        viewModel.getUserInfo(AppHeaders.userMobileNumber).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.nameEditText.text = it.data?.firstName
                    val name = it.data?.firstName?.split("\\s+".toRegex())
                    if (name?.size ?:0 > 1) {
                        val first = name?.getOrNull(0)?.getOrNull(0)?.toString()
                        val second = name?.getOrNull(1)?.getOrNull(0)?.toString()
                        val displayText = first?.capitalize(Locale.ENGLISH) + second?.capitalize(Locale.ENGLISH)
                        binding.nameInitial.text = displayText
                    } else {
                        val first = it.data?.firstName?.getOrNull(0)?.toString()
                        binding.nameInitial.text = first?.capitalize(Locale.ENGLISH)
                    }

                }
                else -> {}
            }
        }
        binding.versions.text = getVersionDetail()
    }

    private fun getVersionDetail(): String {
        return "App Version ${BuildConfig.VERSION_CODE} (${BuildConfig.VERSION_NAME})"
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
