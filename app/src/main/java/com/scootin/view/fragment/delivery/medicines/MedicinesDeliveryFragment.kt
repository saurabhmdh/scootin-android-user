package com.scootin.view.fragment.delivery.medicines

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.scootin.R
import com.scootin.databinding.FragmentMedicinesDeliveryBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.ShopSearchAdapter
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MedicinesDeliveryFragment : Fragment(R.layout.fragment_medicines_delivery) {
    private var binding by autoCleared<FragmentMedicinesDeliveryBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var shopSearchAdapter: ShopSearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMedicinesDeliveryBinding.bind(view)

        updateUI()
        updateListeners()
        binding.storeList.updateVisibility(true)

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
        binding.back.setOnClickListener { findNavController().popBackStack() }

        viewModel.shops.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                shopSearchAdapter.submitList(it.body())
            }
            Timber.i("Search result for shop ${it.body()}")
        }

        viewModel.addToCartMap.observe(viewLifecycleOwner, {
            Timber.i("Status addToCartLiveData = ${it?.isSuccessful} ")

            if (it?.isSuccessful == true) {
                val snack = Snackbar.make(requireView(), "Item added in cart", Snackbar.LENGTH_LONG)
                snack.setAction("VIEW") {
                    val navOptions =
                        NavOptions.Builder().setPopUpTo(R.id.titleScreen, false).build()
                    findNavController().navigate(R.id.cart, null, navOptions)
                }.setBackgroundTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.backgroundTint
                    )
                ).setActionTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.actionTextColor
                    )
                )
                snack.show()
            } else {
                Toast.makeText(requireContext(), it?.errorBody()?.string(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun setStoreAdapter() {
        shopSearchAdapter = ShopSearchAdapter(
            appExecutors, object : ShopSearchAdapter.StoreImageAdapterClickListener {
                override fun onSelectButtonSelected(shopInfo: SearchShopsByCategoryResponse) {
                    Timber.i("Shop Info $shopInfo")
                    val direction =
                        MedicinesDeliveryFragmentDirections.actionMedicineToExpressOrders2(
                            shopInfo.shopID,
                            shopInfo.name
                        )
                    findNavController().navigate(direction)
                }
            })
        binding.storeList.apply {
            adapter = shopSearchAdapter
        }
    }

}