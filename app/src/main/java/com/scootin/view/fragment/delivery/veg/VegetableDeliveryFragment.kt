package com.scootin.view.fragment.delivery.veg

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentVegetableDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.sweets.SweetsItem
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.VegItemAddAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VegetableDeliveryFragment : Fragment(R.layout.fragment_vegetable_delivery) {
    private var binding by autoCleared<FragmentVegetableDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var snacksItemAddAdapter: VegItemAddAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVegetableDeliveryBinding.bind(view)
        setAdaper()
        snacksItemAddAdapter.submitList(setList())
    }

    private fun setAdaper() {
        snacksItemAddAdapter =
            VegItemAddAdapter(
                appExecutors,
                object : VegItemAddAdapter.ImageAdapterClickLister {
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