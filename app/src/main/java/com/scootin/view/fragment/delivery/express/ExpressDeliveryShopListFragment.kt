package com.scootin.view.fragment.delivery.express

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentExpressDeliveryShoplistBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.ShopSearchAdapter
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class ExpressDeliveryShopListFragment : Fragment(R.layout.fragment_express_delivery_shoplist) {
    private var binding by autoCleared<FragmentExpressDeliveryShoplistBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private var shopSearchAdapter by autoCleared<ShopSearchAdapter>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpressDeliveryShoplistBinding.bind(view)
        updateUI()
        updateListeners()
    }

    private fun updateUI() {
        setStoreAdapter()
    }

    private fun updateListeners() {
        //When the screen load lets load the data for empty screen
        viewModel.doSearch("")

        binding.searchBox.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        viewModel.doSearch(query)
                        Timber.i("onQueryTextSubmit $query ")
                    }else{
                        Toast.makeText(context,"Enter text to search",Toast.LENGTH_SHORT).show()
                    }
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
                    viewModel.updateShop(shopInfo.shopID)
                    binding.storeList.updateVisibility(false)
                }
            })
        binding.storeList.apply {
            adapter = shopSearchAdapter
        }
    }
}

