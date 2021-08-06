package com.scootin.view.fragment.delivery.essential

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
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
import com.scootin.view.custom.CustomSearchView
import com.scootin.view.fragment.BaseFragment
import com.scootin.view.vo.GroceryFilters
import com.scootin.view.vo.ProductSearchVO
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class EssentialsGroceryDeliveryFragment : BaseFragment(R.layout.fragment_grocery_delivery) {
    private var binding by autoCleared<FragmentGroceryDeliveryBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    private var productSearchAdapter by autoCleared<ProductSearchPagingAdapter>()
    private var shopSearchAdapter by autoCleared<ShopSearchAdapter>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroceryDeliveryBinding.bind(view)
        updateUI()
        setHasOptionsMenu(true)
        updateListeners()


    }

    private fun updateUI() {
        selectSubCategory(GroceryFilters.fromValue(viewModel.selectedCategoryId), true)
        setStoreAdapter()
        setProductAdapter()
    }

    private fun updateListeners() {
        //When the screen load lets load the data for empty screen
        binding.productList.layoutManager = GridLayoutManager(context,2)
        binding.storeList.layoutManager = GridLayoutManager(context,2)
        viewModel.doSearch("")
        viewModel.loadCount()
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        showLoading()
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
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            when (optionId) {
                R.id.by_product -> {
                    binding.productList.updateVisibility(true)
                    binding.storeList.updateVisibility(false)
                    binding.layoutBag.fabCart.updateVisibility(true)
                }
                R.id.by_store -> {
                    binding.productList.updateVisibility(false)
                    binding.storeList.updateVisibility(true)
                    binding.layoutBag.fabCart.updateVisibility(false)
                }
            }
        }


        viewModel.shopsBySubcategory.observe(viewLifecycleOwner) {response->
            dismissLoading()
            lifecycleScope.launch {
                shopSearchAdapter.submitData(response)
            }
        }

        //We need to check which sub category has been selected..
        viewModel.allProductBySubCategory.observe(viewLifecycleOwner) {response->
            dismissLoading()
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

        setupSubCategoryListener(binding.searchBox)
    }

    private fun setupSubCategoryListener(searchBox: CustomSearchView) {

        binding.allEssentials.setOnClickListener {
            if (binding.allEssentials.isSelected) {
                return@setOnClickListener
            }
            selectSubCategory(GroceryFilters.ALL, false)
        }

        binding.grocery.setOnClickListener {
            val subcategory = it.tag as String?
            if (binding.grocery.isSelected || subcategory.isNullOrEmpty()) {
                return@setOnClickListener
            }
            selectSubCategory(GroceryFilters.GROCERY, false)
        }

        binding.breakfast.setOnClickListener {
            val subcategory = it.tag as String?
            if (binding.breakfast.isSelected || subcategory.isNullOrEmpty()) {
                return@setOnClickListener
            }
            selectSubCategory(GroceryFilters.BREAKFAST, false)
        }
        binding.household.setOnClickListener {
            val subcategory = it.tag as String?
            if (binding.household.isSelected || subcategory.isNullOrEmpty()) {
                return@setOnClickListener
            }
            selectSubCategory(GroceryFilters.HOUSEHOLD, false)
        }

        binding.hygiene.setOnClickListener {
            val subcategory = it.tag as String?
            if (binding.hygiene.isSelected || subcategory.isNullOrEmpty()) {
                return@setOnClickListener
            }
            selectSubCategory(GroceryFilters.HYGIENE, false)
        }
    }

    private fun selectSubCategory(filter: GroceryFilters, isFirstRun: Boolean) {
        if (isFirstRun.not()) {
            clearPagingData()
        }
        var subcategory = listOf<String>()
        viewModel.selectedCategoryId = filter.value

        when(filter) {
            GroceryFilters.ALL -> {
                subcategory = listOf(
                    "300",
                    "301",
                    "302",
                    "303"
                )
                binding.allEssentials.isSelected = true
                binding.grocery.isSelected = false
                binding.breakfast.isSelected = false
                binding.household.isSelected = false
                binding.hygiene.isSelected = false
            }
            GroceryFilters.GROCERY -> {
                subcategory = listOf(
                    "300"
                )
                binding.grocery.isSelected = true
                binding.breakfast.isSelected = false
                binding.household.isSelected = false
                binding.hygiene.isSelected = false
                binding.allEssentials.isSelected = false
            }
            GroceryFilters.BREAKFAST -> {
                subcategory = listOf(
                    "301"
                )
                binding.grocery.isSelected = false
                binding.breakfast.isSelected = true
                binding.household.isSelected = false
                binding.hygiene.isSelected = false
                binding.allEssentials.isSelected = false
            }
            GroceryFilters.HOUSEHOLD -> {
                subcategory = listOf(
                    "302"
                )
                binding.grocery.isSelected = false
                binding.breakfast.isSelected = false
                binding.household.isSelected = true
                binding.hygiene.isSelected = false
                binding.allEssentials.isSelected = false
            }
            GroceryFilters.HYGIENE -> {
                subcategory = listOf(
                    "303"
                )
                binding.grocery.isSelected = false
                binding.breakfast.isSelected = false
                binding.household.isSelected = false
                binding.hygiene.isSelected = true
                binding.allEssentials.isSelected = false
            }
        }
        viewModel.executeNewRequest(subcategory, binding.searchBox.query?.toString().orEmpty())
    }

    private fun clearPagingData() {
        lifecycleScope.launch {
            shopSearchAdapter.submitData(PagingData.empty())
            productSearchAdapter.submitData(PagingData.empty())
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
                    try {
                        val addToCartRequest = AddToCartRequest(
                            AppHeaders.userID.toInt(),
                            item?.id,
                            count
                        )
                        viewModel.addToCart(addToCartRequest)
                    } catch (e: Exception) {}
                }

                override fun onDecrementItem(
                    view: View,
                    item: ProductSearchVO?,
                    count: Int
                ) {
                    Timber.i("Saurabh onDecrementItem $count")
                    try {
                        val addToCartRequest = AddToCartRequest(
                            AppHeaders.userID.toInt(),
                            item?.id,
                            count
                        )
                        viewModel.addToCart(addToCartRequest)
                    } catch (e: Exception) {}
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
                    findNavController().navigate(EssentialsGroceryDeliveryFragmentDirections.grossaryToShopList(shopInfo.name, shopInfo.shopID, "Essential & Grocery"))
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

