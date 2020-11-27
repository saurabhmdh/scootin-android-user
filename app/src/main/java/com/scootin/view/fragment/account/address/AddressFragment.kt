package com.scootin.view.fragment.account.address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.scootin.R
import com.scootin.databinding.FragmentAddNewAddressBinding
import com.scootin.databinding.FragmentAddressBinding
import com.scootin.extensions.setNavigationResult
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddToCartRequest
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.util.Conversions
import com.scootin.util.constants.AppConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.AddressAdapter
import com.scootin.view.adapter.SweetsAdapter
import com.scootin.view.fragment.BaseFragment
import com.scootin.view.vo.AddressVo
import com.scootin.viewmodel.account.AddressFragmentViewModel
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AddressFragment : BaseFragment(R.layout.fragment_address) {

    private var binding by autoCleared<FragmentAddressBinding>()
    private val viewModel: AddressFragmentViewModel by viewModels()

    private lateinit var addressAdapter: AddressAdapter
    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddressBinding.bind(view)
        setAdapter()
        setupListener()

    }

    private fun setupListener() {
        viewModel.loadAddress()
        viewModel.getAddressLiveData().observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val addressList = mutableListOf<AddressVo>().apply {
                    it.body()?.forEach { data ->
                        add(AddressVo(data))
                    }
                }
                addressAdapter.submitList(addressList)
                Timber.i("Data $addressList")
                // Need to send data to adapter..
            } else {
                Toast.makeText(requireContext(), "There is some error while getting address", Toast.LENGTH_SHORT).show()
            }
        }

        binding.addNewAddress.setOnClickListener {
            findNavController().navigate(AddressFragmentDirections.addressFragmentToAddNew(null))
        }
    }

    private fun setAdapter() {
        addressAdapter = AddressAdapter(
            appExecutors,
            object : AddressAdapter.IClickLister {
                override fun onCreateIcon(address: AddressVo, position: Int) {
                    Timber.i("onCreateIcon item is $address")
                    val direction = AddressFragmentDirections.addressFragmentToAddNew(address.addressDetail)
                    findNavController().navigate(direction)
                }

                override fun onDeleteIcon(address: AddressVo, position: Int) {
                    Timber.i("onCreateIcon item is $address")
                    showLoading()
                    viewModel.deleteAddress(AppHeaders.userID, address.id.toString()).observe(viewLifecycleOwner) {
                        dismissLoading()
                        if (it.isSuccessful) {
                            Timber.i("Its successful deleted")
                            viewModel.loadAddress()
                        } else {
                            Toast.makeText(requireContext(), it.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun checkboxSelected(address: AddressVo, position: Int) {
                    setNavigationResult(Gson().toJson(address.addressDetail))
                    findNavController().popBackStack()
                }

            })

        binding.recycler.apply {
            adapter = addressAdapter
        }
    }

}