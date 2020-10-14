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
import com.scootin.network.AppExecutors
import com.scootin.network.response.sweets.SweetsItem
import com.scootin.network.response.sweets.SweetsStore
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.*
import com.scootin.view.fragment.dialogs.EssentialCategoryDialogFragment
import com.scootin.viewmodel.delivery.essential.EssentialsGroceryDeliveryViewModel
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class EssentialsGroceryDeliveryFragment : Fragment(R.layout.fragment_grocery_delivery) {
    private var binding by autoCleared<FragmentGroceryDeliveryBinding>()
    private val viewModel: EssentialsGroceryDeliveryViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
//    private lateinit var essentialAdapter: EssentialGroceryAddAdapter

    private lateinit var essentialGroceryStoreAdapter: EssentialGroceryStoreAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroceryDeliveryBinding.bind(view)

        updateListeners()



//        setAdaper()
//        essentialAdapter.submitList(setList())
//        binding.radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
//            when (optionId) {
//                R.id.materialRadioButton -> {
//                    setAdaper()
//                    essentialAdapter.submitList(setList())
//                }
//                R.id.materialRadioButton2 -> {
//                    setStoreAdapter()
//                    essentialGroceryStoreAdapter.submitList(setStoreList())
//                }
//            }
//        }
    }

    private fun updateListeners() {
        binding.srchsweets.setOnQueryTextListener(
            object :SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Timber.i("onQueryTextSubmit $query")
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    //Timber.i("onQueryTextChange $newText")
                    return false
                }

            }
        )
        binding.back.setOnClickListener { findNavController().popBackStack() }
    }


//    private fun setAdaper() {
//        essentialAdapter =
//            EssentialGroceryAddAdapter(
//                appExecutors,
//                object : EssentialGroceryAddAdapter.ImageAdapterClickLister {
//                    override fun onIncrementItem(view: View) {
//                    }
//
//                    override fun onDecrementItem(view: View) {
//                    }
//
//                })
//
//        binding.list.apply {
//            adapter = essentialAdapter
//        }
//    }
//
//    private fun setStoreAdapter() {
//        essentialGroceryStoreAdapter = EssentialGroceryStoreAdapter(
//            appExecutors,
//            object : EssentialGroceryStoreAdapter.StoreImageAdapterClickListener {
//
//                override fun onSelectButtonSelected(view: View) {
//                }
//
//            })
//        binding.list.apply {
//            adapter = essentialGroceryStoreAdapter
//        }
//    }
//
//
//    private fun setList(): ArrayList<SweetsItem> {
//        val list = ArrayList<SweetsItem>()
//        list.add(
//            SweetsItem(
//                "0",
//                "Brand Name",
//                "Product Name- Type",
//                "MRP Rs 206",
//                "Rs 205",
//                ""
//            )
//        )
//        list.add(
//            SweetsItem(
//                "0",
//                "Aashirvaad",
//                "Atta- Select Whole Wheat",
//                "MRP Rs 225",
//                "Rs 209",
//                ""
//            )
//        )
//        list.add(
//            SweetsItem(
//                "0",
//                "Aashirvaad",
//                "Ataa- Sugar Release Control",
//                "MRP Rs 254",
//                "Rs 250",
//                ""
//            )
//        )
//        list.add(
//            SweetsItem(
//                "0",
//                "patanjali",
//                "Whole Wheat Chakki Fresh",
//                "MRP Rs 206",
//                "Rs 205",
//                ""
//            )
//        )
//        list.add(
//            SweetsItem(
//                "0",
//                "Brand Name",
//                "Product Name- Type",
//                "MRP Rs 206",
//                "Rs 205",
//                ""
//            )
//        )
//        list.add(
//            SweetsItem(
//                "0",
//                "Brand Name",
//                "Product Name- Type",
//                "MRP Rs 206",
//                "Rs 205",
//                ""
//            )
//        )
//        list.add(
//            SweetsItem(
//                "0",
//                "Brand Name",
//                "Product Name- Type",
//                "MRP Rs 206",
//                "Rs 205",
//                ""
//            )
//        )
//        list.add(
//            SweetsItem(
//                "0",
//                "Brand Name",
//                "Product Name- Type",
//                "MRP Rs 206",
//                "Rs 205",
//                ""
//            )
//        )
//        list.add(
//            SweetsItem(
//                "0",
//                "Brand Name",
//                "Product Name- Type",
//                "MRP Rs 206",
//                "Rs 205",
//                ""
//            )
//        )
//
//        return list
//    }
//
//    private fun setStoreList(): ArrayList<SweetsStore> {
//        val storelist = ArrayList<SweetsStore>()
//        storelist.add(
//            SweetsStore("0", "Business Name", "500m", 4f,true,"")
//        )
//        storelist.add(
//            SweetsStore("0", "Business Name", "500m", 4.4f,false,"")
//        )
//        storelist.add(
//            SweetsStore("0", "Business Name", "500m", 4.3f,true,"")
//        )
//        storelist.add(
//            SweetsStore("0", "Business Name", "500m", 3.6f,true,"")
//        )
//        storelist.add(
//            SweetsStore("0", "Business Name", "500m", 4.8f,true,"")
//        )
//        storelist.add(
//            SweetsStore("0", "Business Name", "500m", 4.2f,true,"")
//        )
//        storelist.add(
//            SweetsStore("0", "Business Name", "500m", 4.0f,true,"")
//        )
//        return storelist
//
//    }
}

