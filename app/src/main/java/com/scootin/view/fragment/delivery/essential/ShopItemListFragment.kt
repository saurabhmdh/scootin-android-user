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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.request.RequestOptions
import com.scootin.R
import com.scootin.databinding.FragmentEssentialShopItemListBinding
import com.scootin.databinding.FragmentVegetableDeliveryBinding
import com.scootin.extensions.orZero
import com.scootin.network.AppExecutors
import com.scootin.network.glide.GlideApp
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddToCartRequest
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.ProductSearchPagingAdapter
import com.scootin.view.fragment.orders.DirectOrderSummaryFragmentArgs
import com.scootin.view.vo.ProductSearchVO
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class ShopItemListFragment: Fragment(R.layout.fragment_essential_shop_item_list) {


    private var binding by autoCleared<FragmentEssentialShopItemListBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    private var productSearchAdapter by autoCleared<ProductSearchPagingAdapter>()

    private val args: ShopItemListFragmentArgs by navArgs()

    private val shopId by lazy {
        args.shopId
    }

    private val name by lazy {
        args.name
    }

    private val imageUrl by lazy {
        args.url
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEssentialShopItemListBinding.bind(view)
        updateUI()
        updateListeners()
        viewModel.loadCount()
    }


    private fun updateUI() {
        setProductAdapter()
        loadMedia()
    }

    private fun loadMedia() {
        binding.storeName.text = name
        GlideApp.with(requireContext()).load(imageUrl).apply(RequestOptions().override(dpToPx(R.dimen.image_width), dpToPx(R.dimen.image_height))).into(binding.express)
    }

    fun dpToPx(resource: Int): Int {
        return resources.getDimensionPixelOffset(resource)
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
        viewModel.doSearchByShop("", shopId)

        binding.searchBox.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.doSearchByShop(it, shopId)
                    }
                    Timber.i("onQueryTextSubmit $query ")
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        viewModel.doSearchByShop("", shopId)
                    }
                    return false
                }

            }
        )
        binding.back.setOnClickListener { findNavController().popBackStack() }

        viewModel.allProductByShop.observe(viewLifecycleOwner) {response->
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