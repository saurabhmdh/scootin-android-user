package com.scootin.view.fragment.delivery.essential

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentGroceryDeliveryBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
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
    private lateinit var essentialAdapter: EssentialGroceryAddAdapter
    private lateinit var shopSearchAdapter: ShopSearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroceryDeliveryBinding.bind(view)
        updateUI()
        updateListeners()


        essentialAdapter.submitList(setList())
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
    }


    private fun setProductAdapter() {
        essentialAdapter = EssentialGroceryAddAdapter(
                appExecutors,
                object : EssentialGroceryAddAdapter.ImageAdapterClickLister {
                    override fun onIncrementItem(view: View) {
                    }

                    override fun onDecrementItem(view: View) {
                    }

                })

        binding.productList.apply {
            adapter = essentialAdapter
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


    private fun setList(): ArrayList<SweetsItem> {
        val list = ArrayList<SweetsItem>()
        list.add(
            SweetsItem(
                "0",
                "Brand Name",
                "Product Name- Type",
                "MRP Rs 206",
                "Rs 205",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Aashirvaad",
                "Atta- Select Whole Wheat",
                "MRP Rs 225",
                "Rs 209",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Aashirvaad",
                "Ataa- Sugar Release Control",
                "MRP Rs 254",
                "Rs 250",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "patanjali",
                "Whole Wheat Chakki Fresh",
                "MRP Rs 206",
                "Rs 205",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Brand Name",
                "Product Name- Type",
                "MRP Rs 206",
                "Rs 205",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Brand Name",
                "Product Name- Type",
                "MRP Rs 206",
                "Rs 205",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Brand Name",
                "Product Name- Type",
                "MRP Rs 206",
                "Rs 205",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Brand Name",
                "Product Name- Type",
                "MRP Rs 206",
                "Rs 205",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Brand Name",
                "Product Name- Type",
                "MRP Rs 206",
                "Rs 205",
                ""
            )
        )

        return list
    }
}

