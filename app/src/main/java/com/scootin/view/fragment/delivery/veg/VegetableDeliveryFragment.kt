package com.scootin.view.fragment.delivery.veg


import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.scootin.R
import com.scootin.databinding.FragmentVegetableDeliveryBinding
import com.scootin.extensions.orZero
import com.scootin.network.AppExecutors
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddToCartRequest
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.ProductSearchPagingAdapter
import com.scootin.view.custom.CustomSearchView
import com.scootin.view.vo.ProductSearchVO
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class VegetableDeliveryFragment : Fragment(R.layout.fragment_vegetable_delivery) {
    private var binding by autoCleared<FragmentVegetableDeliveryBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    private var productSearchAdapter by autoCleared<ProductSearchPagingAdapter>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVegetableDeliveryBinding.bind(view)
        updateUI()
        updateListeners()

        viewModel.loadCount()
    }

    private fun updateUI() {
        binding.veg.isSelected = true
        val defaultItem = listOf("400")
        viewModel.updateSubCategory(defaultItem)
        binding.productList.layoutManager = GridLayoutManager(context,2)
        setProductAdapter()
    }

    private fun setProductAdapter() {
        productSearchAdapter = ProductSearchPagingAdapter(
            object : ProductSearchPagingAdapter.ImageAdapterClickLister {
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

        viewModel.allProductBySubCategory.observe(viewLifecycleOwner) {response->
            lifecycleScope.launch {
                productSearchAdapter.submitData(response)
            }
        }

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
        viewModel.addToCartMap.observe(viewLifecycleOwner, {
            Timber.i("Status addToCartLiveData = ${it?.isSuccessful} ")
            viewModel.loadCount()
        })

        setupSubCategoryListener(binding.searchBox)
    }

    private fun setupSubCategoryListener(searchBox: CustomSearchView) {

        binding.veg.setOnClickListener {
            val subcategory = it.tag as String?
            if (binding.veg.isSelected || subcategory.isNullOrEmpty()) {
                return@setOnClickListener
            }
            clearPagingData()
            Timber.i("Selected.. ${it.tag as String?}")
            viewModel.executeNewRequest(listOf(subcategory), searchBox.query?.toString().orEmpty())
            binding.veg.isSelected = true
            //binding.allCategories.isSelected=false
            binding.fruits.isSelected = false

        }

        binding.fruits.setOnClickListener {
            val subcategory = it.tag as String?
            if (binding.fruits.isSelected || subcategory.isNullOrEmpty()) {
                return@setOnClickListener
            }
            clearPagingData()
            Timber.i("Selected.. ${it.tag as String?}")
            viewModel.executeNewRequest(listOf(subcategory), searchBox.query?.toString().orEmpty())
            binding.veg.isSelected = false
            //binding.allCategories.isSelected=false
            binding.fruits.isSelected = true


        }

//        binding.allCategories.setOnClickListener {
//            if (binding.allCategories.isSelected) {
//                return@setOnClickListener
//            }
//            clearPagingData()
//            Timber.i("Selected.. ${it.tag as String?}")
//            viewModel.executeNewRequest(it.tag as String?, searchBox.query?.toString().orEmpty())
//            binding.veg.isSelected = false
//            binding.fruits.isSelected = false
//            binding.allCategories.isSelected=true
//
//
//        }

    }

    private fun clearPagingData() {
        lifecycleScope.launch {
            productSearchAdapter.submitData(PagingData.empty())
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