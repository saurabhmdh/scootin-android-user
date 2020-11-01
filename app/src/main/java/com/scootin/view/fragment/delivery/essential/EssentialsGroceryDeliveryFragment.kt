package com.scootin.view.fragment.delivery.essential

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentGroceryDeliveryBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddToCartRequest
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.network.response.sweets.SweetsItem
import com.scootin.network.response.sweets.SweetsStore
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.*
import com.scootin.view.fragment.dialogs.EssentialCategoryDialogFragment
import com.scootin.viewmodel.delivery.CategoriesViewModel
import com.scootin.viewmodel.delivery.essential.EssentialsGroceryDeliveryViewModel
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class EssentialsGroceryDeliveryFragment : Fragment(R.layout.fragment_grocery_delivery) {
    private var binding by autoCleared<FragmentGroceryDeliveryBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var productSearchAdapter: ProductSearchAdapter
    private lateinit var shopSearchAdapter: ShopSearchAdapter

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

        viewModel.addToCartMap.observe(viewLifecycleOwner, Observer {
            Timber.i("Status addToCartLiveData = ${it.isSuccessful} ")
        })
    }

    private fun updateUI() {
        setStoreAdapter()
        setProductAdapter()
    }

    private fun updateListeners() {
        //When the screen load lets load the data for empty screen
        viewModel.doSearch("")

        binding.searchBox.setOnQueryTextListener(
            object :SearchView.OnQueryTextListener {
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
                    return false
                }

            }
        )
        binding.back.setOnClickListener { findNavController().popBackStack() }

        viewModel.shops.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                shopSearchAdapter.submitList(it.body())
            }
            Timber.i("Search result for shop ${it.body()}")
        }

        viewModel.product.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                productSearchAdapter.submitList(it.body())
            }
        }
    }


    private fun setProductAdapter() {
        productSearchAdapter = ProductSearchAdapter(
                appExecutors,
                object : ProductSearchAdapter.ImageAdapterClickLister {
                    override fun onIncrementItem(
                        view: View,
                        item: SearchProductsByCategoryResponse?,
                        count: Int
                    ) {
                        val addToCartRequest = AddToCartRequest(AppHeaders.userID.toInt(), item?.id, count)
                        viewModel.addToCart(addToCartRequest)
                    }

                    override fun onDecrementItem(
                        view: View,
                        item: SearchProductsByCategoryResponse?,
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
        shopSearchAdapter = ShopSearchAdapter(
            appExecutors, object : ShopSearchAdapter.StoreImageAdapterClickListener {
                override fun onSelectButtonSelected(view: View) {
                    //TODO: We need to go for search Product in this store..
                }
            })
        binding.storeList.apply {
            adapter = shopSearchAdapter
        }
    }
}

