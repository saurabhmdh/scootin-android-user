package com.scootin.view.fragment.delivery.sweet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentStationaryDeliveryBinding
import com.scootin.databinding.FragmentSweetsDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.sweets.SweetsItem
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.SweetsItemAddAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SweetsDeliveryFragment : Fragment(R.layout.fragment_sweets_delivery) {
    private var binding by autoCleared<FragmentSweetsDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var sweetsAdapter: SweetsItemAddAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSweetsDeliveryBinding.bind(view)
        setAdaper()
        sweetsAdapter.submitList(setList())

    }

    private fun setAdaper() {
        sweetsAdapter =
            SweetsItemAddAdapter(
                appExecutors,
                object : SweetsItemAddAdapter.ImageAdapterClickLister {
                    override fun onIncrementItem(view: View) {
                    }

                    override fun onDecrementItem(view: View) {
                    }

                })

        binding.list.apply {
            adapter = sweetsAdapter
        }
    }

    private fun setList(): ArrayList<SweetsItem> {
        val list = ArrayList<SweetsItem>()
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Motichoor Ladoo",
                "MRP 250$",
                "1240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Ladoo",
                "MRP 3350$",
                "2240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Besan Ladoo",
                "MRP 230$",
                "2240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Kajoo Barfi",
                "MRP 350$",
                "2240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Barfi",
                "MRP 3250$",
                "2240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Gulab Jamun",
                "MRP 3250$",
                "2240$",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Peda",
                "MRP 3250$",
                "2240$",
                ""
            )
        )
        return list
    }

}