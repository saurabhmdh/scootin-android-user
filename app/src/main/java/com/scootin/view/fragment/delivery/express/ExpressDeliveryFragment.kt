package com.scootin.view.fragment.delivery.express

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.scootin.R
import com.scootin.databinding.FragmentExpressDeliveryBinding
import com.scootin.databinding.FragmentExpressDeliveryShopSelectBinding
import com.scootin.databinding.FragmentGroceryDeliveryShopSelectBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.network.response.sweets.SweetsStore
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.EssentialGroceryStoreAdapter
import com.scootin.view.adapter.ShopSearchAdapter
import com.scootin.view.fragment.delivery.essential.EssentialSelectShopFragmentDirections
import com.scootin.view.fragment.home.HomeFragmentDirections
import com.scootin.viewmodel.delivery.CategoriesViewModel
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ExpressDeliveryFragment : Fragment(R.layout.fragment_express_delivery_shop_select) {
    private var binding by autoCleared<FragmentExpressDeliveryShopSelectBinding>()
    @Inject
    lateinit var appExecutors: AppExecutors
    private var shopSearchAdapter by autoCleared<ShopSearchAdapter>()
    private val viewModel: CategoriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpressDeliveryShopSelectBinding.bind(view)
        binding.storeList.layoutManager = GridLayoutManager(context,2)
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

        binding.searchBox.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    query?.let {
                        viewModel.doSearch(it)
                    }


                    Timber.i("onQueryTextSubmit $query ")
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        viewModel.doSearch("")
                    }
                    return false
                }
            }
        )
    }

    private fun setStoreAdapter() {
        shopSearchAdapter = ShopSearchAdapter(object : ShopSearchAdapter.StoreImageAdapterClickListener {
                override fun onSelectButtonSelected(shopInfo: SearchShopsByCategoryResponse) {
                    Timber.i("Shop Info $shopInfo")
                    val direction = ExpressDeliveryFragmentDirections.actionExpressDeliveryToExpressOrders2(shopInfo.shopID, shopInfo.name)
                    findNavController().navigate(direction)
                }
            })
        binding.storeList.apply {
            adapter = shopSearchAdapter
        }
    }
}

