package com.scootin.view.fragment.delivery.essential

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentGroceryDeliveryBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.extensions.orZero
import com.scootin.extensions.updateVisibility
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddToCartRequest
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.ProductSearchPagingAdapter
import com.scootin.view.adapter.ShopSearchAdapter
import com.scootin.view.vo.ProductSearchVO
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class EssentialsGroceryDeliveryFragment : Fragment(R.layout.fragment_grocery_delivery) {
    private var binding by autoCleared<FragmentGroceryDeliveryBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    private var productSearchAdapter by autoCleared<ProductSearchPagingAdapter>()
    private var shopSearchAdapter by autoCleared<ShopSearchAdapter>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroceryDeliveryBinding.bind(view)
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

        viewModel.shops.observe(viewLifecycleOwner) {response->
            lifecycleScope.launch {
                shopSearchAdapter.submitData(response)
            }
        }

        viewModel.allProduct.observe(viewLifecycleOwner) {response->
            lifecycleScope.launch {
                productSearchAdapter.submitData(response)
            }
        }

        viewModel.addToCartMap.observe(viewLifecycleOwner, {
            Timber.i("Status addToCartLiveData = ${it?.isSuccessful} ")
            viewModel.loadCount()
        })

        binding.layoutBag.goToBag.setOnClickListener {
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
        productSearchAdapter = ProductSearchPagingAdapter(
            object : ProductSearchPagingAdapter.ImageAdapterClickLister {
                override fun onIncrementItem(
                    view: View,
                    item: ProductSearchVO?,
                    count: Int
                ) {
                    Timber.i("Saurabh onIncrementItem $count")
                    //Increment and decrement will be done one by one
                    val addToCartRequest = AddToCartRequest(
                        AppHeaders.userID.toInt(),
                        item?.id,
                        count
                    )
                    viewModel.addToCart(addToCartRequest)
                }

                override fun onDecrementItem(
                    view: View,
                    item: ProductSearchVO?,
                    count: Int
                ) {
                    Timber.i("Saurabh onDecrementItem $count")
                    val addToCartRequest = AddToCartRequest(
                        AppHeaders.userID.toInt(),
                        item?.id,
                        count
                    )
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
                    //We need to move another screen
                    findNavController().navigate(EssentialsGroceryDeliveryFragmentDirections.grossaryToShopList(shopInfo.name, shopInfo.shopID, shopInfo.imageUrl))
                }
            })
        binding.storeList.apply {
            adapter = shopSearchAdapter
        }
    }

    private fun setupBadge(result: Int) {
        if (result == 0) {
            binding.layoutBag.textCount.visibility = View.GONE
        } else {
            binding.layoutBag.textCount.visibility = View.VISIBLE
            binding.layoutBag.textCount.text = result.toString()
        }
    }
}

