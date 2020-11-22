package com.scootin.view.fragment.delivery.veg

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentVegetableDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddToCartRequest
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.ProductSearchAdapter
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class VegetableDeliveryFragment : Fragment(R.layout.fragment_vegetable_delivery) {
    private var binding by autoCleared<FragmentVegetableDeliveryBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var productSearchAdapter: ProductSearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVegetableDeliveryBinding.bind(view)
        updateUI()
        updateListeners()

        viewModel.addToCartMap.observe(viewLifecycleOwner, {
            Timber.i("Status addToCartLiveData = ${it?.isSuccessful} ")
        })
    }
    private fun updateUI() {
        setProductAdapter()
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

    private fun updateListeners() {
        //When the screen load lets load the data for empty screen
        viewModel.doSearch("")

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
                    return false
                }

            }
        )
        binding.back.setOnClickListener { findNavController().popBackStack() }

        viewModel.product.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                productSearchAdapter.submitList(it.body())
            }
        }
    }


}