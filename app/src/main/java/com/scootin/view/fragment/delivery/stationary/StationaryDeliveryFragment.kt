package com.scootin.view.fragment.delivery.stationary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentStationaryDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.stationary.StationaryItem
import com.scootin.network.response.sweets.SweetsStore
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.EssentialGroceryStoreAdapter
import com.scootin.view.adapter.StationaryItemAddAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StationaryDeliveryFragment : Fragment(R.layout.fragment_stationary_delivery) {
    private var binding by autoCleared<FragmentStationaryDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var stationaryAdapter: StationaryItemAddAdapter
    private lateinit var stationaryStoreAdapter: EssentialGroceryStoreAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStationaryDeliveryBinding.bind(view)
        setAdaper()
        stationaryAdapter.submitList(setList())
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            when (optionId) {
                R.id.materialRadioButton -> {
                    setAdaper()
                    stationaryAdapter.submitList(setList())
                }
                R.id.materialRadioButton2 -> {
                    setStoreAdapter()
                    stationaryStoreAdapter.submitList(setStoreList())
                }
            }
        }
    }

    private fun setAdaper() {
        stationaryAdapter =
            StationaryItemAddAdapter(
                appExecutors,
                object : StationaryItemAddAdapter.ImageAdapterClickLister {
                    override fun onIncrementItem(view: View) {
                    }

                    override fun onDecrementItem(view: View) {
                    }

                })

        binding.list.apply {
            adapter = stationaryAdapter
        }
    }
    private fun setStoreAdapter() {
        stationaryStoreAdapter = EssentialGroceryStoreAdapter(
            appExecutors,
            object : EssentialGroceryStoreAdapter.StoreImageAdapterClickListener {

                override fun onSelectButtonSelected(view: View) {
                }

            })
        binding.list.apply {
            adapter = stationaryStoreAdapter
        }
    }

    private fun setList(): ArrayList<StationaryItem> {
        val list = ArrayList<StationaryItem>()
        list.add(
            StationaryItem(
                "0",
                "Mansa Stationaries Classic",
                "Classic",
                "500 sheet paper rim",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "Mansa Stationaries Classic",
                "Classic",
                "500 sheet paper rim",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "Mansa Stationaries Classic",
                "Classic",
                "500 sheet paper rim",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "Mansa Stationaries Classic",
                "Classic",
                "500 sheet paper rim",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "Mansa Stationaries Classic",
                "Classic",
                "500 sheet paper rim",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "Mansa Stationaries Classic",
                "Classic",
                "500 sheet paper rim",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "Mansa Stationaries Classic",
                "Classic",
                "500 sheet paper rim",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "Mansa Stationaries Classic",
                "Classic",
                "500 sheet paper rim",
                "MRP: Rs 124",
                "Rs 110",
                ""
            )
        )
        list.add(
            StationaryItem(
                "0",
                "Mansa Stationaries Classic",
                "Classic",
                "500 sheet paper rim",
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