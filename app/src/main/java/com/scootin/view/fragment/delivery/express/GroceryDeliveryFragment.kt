package com.scootin.view.fragment.delivery.express

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentGroceryDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.stationary.StationaryItem
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.StationaryItemAddAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GroceryDeliveryFragment : Fragment(R.layout.fragment_grocery_delivery) {
    private var binding by autoCleared<FragmentGroceryDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var stationaryAdapter: StationaryItemAddAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroceryDeliveryBinding.bind(view)
        setAdaper()
        stationaryAdapter.submitList(setList())
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

//        binding.list.apply {
//            adapter = stationaryAdapter
//        }
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

}