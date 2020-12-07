package com.scootin.view.fragment.delivery.sweet

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentSweetsDeliveryBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.extensions.orZero
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddToCartRequest
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.ShopSearchAdapter
import com.scootin.view.adapter.SweetsAdapter
import com.scootin.view.fragment.delivery.essential.EssentialsGroceryDeliveryFragmentDirections
import com.scootin.view.vo.ProductSearchVO
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SweetsDeliveryFragment : Fragment(R.layout.fragment_sweets_delivery) {
    private var binding by autoCleared<FragmentSweetsDeliveryBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    private var productSearchAdapter by autoCleared<SweetsAdapter>()
    private var shopSearchAdapter by autoCleared<ShopSearchAdapter>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSweetsDeliveryBinding.bind(view)

        updateUI()
        updateListeners()

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            when (optionId) {
                R.id.by_product -> {
                    binding.productList.updateVisibility(true)
                    binding.storeList.updateVisibility(false)
                }
                R.id.by_store -> {
                    binding.productList.updateVisibility(false)
                    binding.storeList.updateVisibility(true)
                }
            }
        }

    }
    private fun updateUI() {
        setStoreAdapter()
        setProductAdapter()
    }

    private fun updateListeners() {
        //When the screen load lets load the data for empty screen
        viewModel.doSearch("")
        viewModel.loadCount()

        binding.searchBox.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    when (binding.radioGroup.getCheckedRadioButtonPosition()) {
                        0 -> {
                            query?.let {
                                viewModel.doSearch(it)
                            }
                        }
                        1 -> {
                            query?.let {
                                viewModel.doSearch(it)
                            }
                        }
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
        binding.back.setOnClickListener { findNavController().popBackStack() }

        viewModel.shopsBySubcategory.observe(viewLifecycleOwner) {response->
            lifecycleScope.launch {
                shopSearchAdapter.submitData(response)
            }
        }

        viewModel.allProductBySubCategoryWithFilter.observe(viewLifecycleOwner) {response->
            lifecycleScope.launch {
                productSearchAdapter.submitData(response)
            }
        }

        viewModel.addToCartMap.observe(viewLifecycleOwner, {
            Timber.i("Status addToCartLiveData = ${it?.isSuccessful} ")
            viewModel.loadCount()
        })

        binding.fabCart.setOnClickListener {
            val navOptions =
                NavOptions.Builder().setPopUpTo(R.id.titleScreen, false).build()
            findNavController().navigate(R.id.cart, null, navOptions)
        }

        viewModel.getCartCount.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val result = it.body()?.toInt().orZero()
                setupBadge(result)
            }
        }
    }


    private fun setProductAdapter() {
        productSearchAdapter = SweetsAdapter(
            object : SweetsAdapter.ImageAdapterClickLister {
                override fun onIncrementItem(
                    view: View,
                    item: ProductSearchVO?,
                    count: Int
                ) {
                    val addToCartRequest = AddToCartRequest(AppHeaders.userID.toInt(), item?.id, count)
                    viewModel.addToCart(addToCartRequest)
                }

                override fun onDecrementItem(
                    view: View,
                    item: ProductSearchVO?,
                    count: Int
                ) {
                    val addToCartRequest = AddToCartRequest(AppHeaders.userID.toInt(), item?.id, count)
                    viewModel.addToCart(addToCartRequest)
                }

            })

        binding.productList.apply {
            adapter = productSearchAdapter
        }
    }

    private fun setStoreAdapter() {
        shopSearchAdapter = ShopSearchAdapter(object : ShopSearchAdapter.StoreImageAdapterClickListener {
                override fun onSelectButtonSelected(shopInfo: SearchShopsByCategoryResponse) {
                    Timber.i("Shop Info $shopInfo")
                    findNavController().navigate(SweetsDeliveryFragmentDirections.sweetToShopList(shopInfo.name, shopInfo.shopID, shopInfo.imageUrl))
                }
            })
        binding.storeList.apply {
            adapter = shopSearchAdapter
        }
    }

    private fun setupBadge(result: Int) {
        if (result == 0) {
            binding.textCount.visibility = View.GONE
        } else {
            binding.textCount.visibility = View.VISIBLE
            binding.textCount.text = result.toString()
        }
    }

}
