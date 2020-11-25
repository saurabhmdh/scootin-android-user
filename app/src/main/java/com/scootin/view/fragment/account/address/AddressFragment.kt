package com.scootin.view.fragment.account.address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
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
        viewModel.addressLiveData.observe(viewLifecycleOwner) {
            if(it.isSuccessful){
                Timber.i(""+it.body())
            }
        }
    }
    private fun setAdapter() {
        addressAdapter = AddressAdapter(
            appExecutors,
            object : AddressAdapter.ImageAdapterClickLister {
                override fun onCreateIcon(view: AddressDetails) {

                }

                override fun onDeleteIcon(view: AddressDetails) {

                }

            })

        binding.recycler.apply {
            adapter = addressAdapter
        }
    }

}