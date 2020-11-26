package com.scootin.view.fragment.account.address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.scootin.R
import com.scootin.databinding.FragmentAddNewAddressBinding
import com.scootin.databinding.FragmentAddressBinding
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
import com.scootin.view.vo.AddressVo
import com.scootin.viewmodel.account.AddressFragmentViewModel
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AddressFragment : Fragment(R.layout.fragment_address) {

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
        viewModel.addressLiveData.observe(viewLifecycleOwner) {
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

        }
    }

    private fun setAdapter() {
        addressAdapter = AddressAdapter(
            appExecutors,
            object : AddressAdapter.IClickLister {
                override fun onCreateIcon(address: AddressVo, position: Int) {
                    Timber.i("onCreateIcon item is $address")
                }

                override fun onDeleteIcon(address: AddressVo, position: Int) {
                    Timber.i("onCreateIcon item is $address")
                }

                override fun checkboxSelected(address: AddressVo, position: Int) {
                    //We should finish the fragment and set the result.


                    Timber.i("$position -> $address")
                    //TODO: We need to just select this address
                    //Unset all other option and select this
                    val temp = addressAdapter.currentList
                    temp.forEach {
                        it.selected = address.id == it.id
                    }
                    Timber.i("templist $temp")
                    addressAdapter.submitList(null)
                    addressAdapter.submitList(temp)
                }

            })

        binding.recycler.apply {
            adapter = addressAdapter
        }
    }

}