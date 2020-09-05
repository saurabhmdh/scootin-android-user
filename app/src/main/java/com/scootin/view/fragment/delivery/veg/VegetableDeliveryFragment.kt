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
    private lateinit var vegItemAddAdapter: VegItemAddAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVegetableDeliveryBinding.bind(view)
        setAdaper()
        vegItemAddAdapter.submitList(setList())
    }

    private fun setAdaper() {
        vegItemAddAdapter =
            VegItemAddAdapter(
                appExecutors,
                object : VegItemAddAdapter.ImageAdapterClickLister {
                    override fun onIncrementItem(view: View) {
                    }

                    override fun onDecrementItem(view: View) {
                    }

                })

        binding.list.apply {
            adapter = vegItemAddAdapter
        }
    }

    private fun setList(): ArrayList<SweetsItem> {
        val list = ArrayList<SweetsItem>()
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Cauliflower",
                "MRP: Rs 25",
                "Rs 22",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Cauliflower",
                "MRP: Rs 25",
                "Rs 22",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Cauliflower",
                "MRP: Rs 25",
                "Rs 22",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Cauliflower",
                "MRP: Rs 25",
                "Rs 22",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Cauliflower",
                "MRP: Rs 25",
                "Rs 22",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Cauliflower",
                "MRP: Rs 25",
                "Rs 22",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Cauliflower",
                "MRP: Rs 25",
                "Rs 22",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Cauliflower",
                "MRP: Rs 25",
                "Rs 22",
                ""
            )
        )
        list.add(
            SweetsItem(
                "0",
                "Business Name",
                "Cauliflower",
                "MRP: Rs 25",
                "Rs 22",
                ""
            )
        )


        return list
    }

}