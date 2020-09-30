package com.scootin.view.fragment.delivery.express

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentExpressDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.network.response.sweets.SweetsStore
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.EssentialGroceryStoreAdapter
import com.scootin.view.adapter.ExpressCategoryAdapter
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ExpressDeliveryCategoryFragment : Fragment(R.layout.fragment_express_delivery) {
    private var binding by autoCleared<FragmentExpressDeliveryBinding>()

    private val viewModel: HomeFragmentViewModel by viewModels()

    var mSelectPos=-1
    private lateinit var homeCategoryList: List<HomeResponseCategory>

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var expressDeliveryAdapter: ExpressCategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpressDeliveryBinding.bind(view)
        setStoreAdapter()
        doNetworkCall()
    }



    private fun setStoreAdapter() {
        expressDeliveryAdapter = ExpressCategoryAdapter(
            appExecutors,ExpressDeliveryCategoryFragment(),
            object : ExpressCategoryAdapter.StoreImageAdapterClickListener {

                override fun onSelectButtonSelected(position: Int) {
                    mSelectPos=position
                    expressDeliveryAdapter.notifyDataSetChanged()
                }

            })
        binding.list.apply {
            adapter = expressDeliveryAdapter
        }
    }


    private fun doNetworkCall() {
        viewModel.getHomeCategory().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {
                }
                Status.SUCCESS -> {
                    Timber.i("Category Response ${it.data}")
                    it.data?.let {list->
                        homeCategoryList = list
                        expressDeliveryAdapter.submitList(homeCategoryList)
                    }
                }
                Status.LOADING -> {
                }
            }
        })

        viewModel.serviceArea.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Congratulation!! We are serving in area = " +it.name, Toast.LENGTH_LONG).show()
        })

        viewModel.serviceAreaError.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Sorry!! Our services are not allowed in this area..\n Please change the location..", Toast.LENGTH_LONG).show()
        })
    }
}

