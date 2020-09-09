package com.scootin.view.fragment.delivery.clothing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentClothingDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.stationary.StationaryItem
import com.scootin.network.response.sweets.SweetsStore
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.EssentialGroceryStoreAdapter
import com.scootin.view.adapter.StationaryItemAddAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ClothingInnerWearMenFragment : Fragment(R.layout.fragment_clothing_delivery) {
    private var binding by autoCleared<FragmentClothingDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var clothingAdapter: StationaryItemAddAdapter
    private lateinit var clothingStoreAdapter: EssentialGroceryStoreAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClothingDeliveryBinding.bind(view)
        setAdaper()
        clothingAdapter.submitList(setList())
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            when (optionId) {
                R.id.materialRadioButton -> {
                    setAdaper()
                    clothingAdapter.submitList(setList())
                }
                R.id.materialRadioButton2 -> {
                    setStoreAdapter()
                    clothingStoreAdapter.submitList(setStoreList())
                }
            }
        }
    }

    private fun setAdaper() {
        clothingAdapter =
            StationaryItemAddAdapter(
                appExecutors,
                object : StationaryItemAddAdapter.ImageAdapterClickLister {
                    override fun onIncrementItem(view: View) {
                    }

                    override fun onDecrementItem(view: View) {
                    }

                })

        binding.list.apply {
            adapter = clothingAdapter
        }
    }
    private fun setStoreAdapter() {
        clothingStoreAdapter = EssentialGroceryStoreAdapter(
            appExecutors,
            object : EssentialGroceryStoreAdapter.StoreImageAdapterClickListener {

                override fun onSelectButtonSelected(view: View) {
                }

            })
        binding.list.apply {
            adapter = clothingStoreAdapter
        }
    }

    private fun setList(): ArrayList<StationaryItem> {
        val list = ArrayList<StationaryItem>()
        list.add(
            StationaryItem(
                "0",
                "business man",
                "brand",
                "Product Description",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "business man",
                "brand",
                "Product Description",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "business man",
                "brand",
                "Product Description",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "business man",
                "brand",
                "Product Description",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "business man",
                "brand",
                "Product Description",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "business man",
                "brand",
                "Product Description",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "business man",
                "brand",
                "Product Description",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "business man",
                "brand",
                "Product Description",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "business man",
                "brand",
                "Product Description",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )



        return list
    }
    private fun setStoreList(): ArrayList<SweetsStore> {
        val storelist = ArrayList<SweetsStore>()
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4f,true,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4.4f,false,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4.3f,true,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 3.6f,true,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4.8f,true,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4.2f,true,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4.0f,true,"")
        )
        return storelist

    }

}