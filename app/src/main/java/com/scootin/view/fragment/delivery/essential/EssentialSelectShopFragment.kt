package com.scootin.view.fragment.delivery.essential

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentGroceryDeliveryShopSelectBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.ShopSearchAdapter
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class EssentialSelectShopFragment : BaseFragment(R.layout.fragment_grocery_delivery_shop_select) {
    private var binding by autoCleared<FragmentGroceryDeliveryShopSelectBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    private var shopSearchAdapter by autoCleared<ShopSearchAdapter>()
    private val args: EssentialSelectShopFragmentArgs by navArgs()

    private val deliverySlot by lazy {
        args.deliverySlot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroceryDeliveryShopSelectBinding.bind(view)
        updateUI()
        updateListeners()

    }

    private fun updateUI() {
        setStoreAdapter()
    }

    private fun updateListeners() {
        //When the screen load lets load the data for empty screen
        viewModel.doSearch("")

        binding.back.setOnClickListener { findNavController().popBackStack() }

        viewModel.shops.observe(viewLifecycleOwner) {response->
            lifecycleScope.launch {
                shopSearchAdapter.submitData(response)
            }
        }
    }



    private fun setStoreAdapter() {
        shopSearchAdapter = ShopSearchAdapter(object : ShopSearchAdapter.StoreImageAdapterClickListener {
                override fun onSelectButtonSelected(shopInfo: SearchShopsByCategoryResponse) {
                    Timber.i("Shop Info $shopInfo")
                    val direction = EssentialSelectShopFragmentDirections.shopSelectionToHandwritten(shopInfo.shopID, shopInfo.name, deliverySlot)
                    findNavController().navigate(direction)
                }
            })
        binding.storeList.apply {
            adapter = shopSearchAdapter
        }
    }
}

