package com.scootin.view.fragment.delivery.veg


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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

    var snack: Snackbar? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVegetableDeliveryBinding.bind(view)
        updateUI()
        updateListeners()


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
                    if (newText.isNullOrEmpty()) {
                        viewModel.doSearch("")
                    }
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

        viewModel.addToCartMap.observe(viewLifecycleOwner, {
            Timber.i("Status addToCartLiveData = ${it?.isSuccessful} ")

            if (it?.isSuccessful == true) {
                snack = Snackbar.make(requireView(), "Item added in cart", Snackbar.LENGTH_INDEFINITE)
                snack?.setAction("VIEW") {
                    val navOptions =
                        NavOptions.Builder().setPopUpTo(R.id.titleScreen, false).build()
                    findNavController().navigate(R.id.cart, null, navOptions)
                }?.setBackgroundTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.backgroundTint
                    )
                )?.setActionTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.actionTextColor
                    )
                )
                snack?.show()
            } else {
                Toast.makeText(requireContext(), it?.errorBody()?.string(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snack?.dismiss()
    }

}