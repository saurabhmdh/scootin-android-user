package com.scootin.view.fragment.delivery.express

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentExpressDeliveryBinding
import com.scootin.databinding.FragmentExpressDeliveryCategoryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.network.response.sweets.SweetsStore
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.EssentialGroceryStoreAdapter
import com.scootin.view.fragment.home.HomeFragmentDirections
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExpressDeliveryFragment : Fragment(R.layout.fragment_express_delivery_category) {
    private var binding by autoCleared<FragmentExpressDeliveryCategoryBinding>()
    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var expressDeliveryAdapter: EssentialGroceryStoreAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpressDeliveryCategoryBinding.bind(view)
        setStoreAdapter()
        expressDeliveryAdapter.submitList(setStoreList())
    }



    private fun setStoreAdapter() {
        expressDeliveryAdapter = EssentialGroceryStoreAdapter(
            appExecutors,
            object : EssentialGroceryStoreAdapter.StoreImageAdapterClickListener {

                override fun onSelectButtonSelected(view: View) {
                    //findNavController().navigate(ExpressDeliveryFragmentDirections.actionExpressDeliveryToExpressOrders2())
                }

            })
        binding.list.apply {
            adapter = expressDeliveryAdapter
        }
    }



    private fun setStoreList(): ArrayList<SweetsStore> {
        val storelist = ArrayList<SweetsStore>()
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4f,true,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4.4f,false,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4.3f,true,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 3.6f,true,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4.8f,true,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4.2f,true,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4.0f,true,"")
        )
        return storelist
    }
}

