package com.scootin.view.fragment.delivery.sweet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentStationaryDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.stationary.StationaryItem
import com.scootin.network.response.sweets.SweetsItem
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.SnacksItemAddAdapter
import com.scootin.view.adapter.StationaryItemAddAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SnacksDeliveryFragment : Fragment(R.layout.fragment_snacks_delivery) {
    private var binding by autoCleared<FragmentStationaryDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var snacksItemAddAdapter: SnacksItemAddAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStationaryDeliveryBinding.bind(view)
        setAdaper()
        snacksItemAddAdapter.submitList(setList())
    }

    private fun setAdaper() {
        snacksItemAddAdapter =
            SnacksItemAddAdapter(
                appExecutors,
                object : SnacksItemAddAdapter.ImageAdapterClickLister {
                    override fun onIncrementItem(view: View) {
                    }

                    override fun onDecrementItem(view: View) {
                    }

                })

        binding.list.apply {
            adapter = snacksItemAddAdapter
        }
    }

    private fun setList(): ArrayList<SweetsItem> {
        val list = ArrayList<SweetsItem>()
        list.add(
            SweetsItem(
                "0",
                "Buisness Name",
                "Motichoor Ladoo",
                "MRP 250$",
                "1240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Buisness Name",
                "Ladoo",
                "MRP 3350$",
                "2240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Buisness Name",
                "Besan Ladoo",
                "MRP 230$",
                "2240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Buisness Name",
                "Kajoo Barfi",
                "MRP 350$",
                "2240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Buisness Name",
                "Barfi",
                "MRP 3250$",
                "2240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Buisness Name",
                "Gulab Jamun",
                "MRP 3250$",
                "2240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Buisness Name",
                "Peda",
                "MRP 3250$",
                "2240$",
                ""
            )
        )
        return list
    }

}