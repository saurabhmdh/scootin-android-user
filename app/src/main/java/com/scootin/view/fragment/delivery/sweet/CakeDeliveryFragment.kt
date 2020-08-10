package com.scootin.view.fragment.delivery.sweet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentCakeDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.sweets.CakeItem
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.CakeItemAddAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CakeDeliveryFragment : Fragment(R.layout.fragment_cake_delivery) {
    private var binding by autoCleared<FragmentCakeDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var cakeAdapter: CakeItemAddAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCakeDeliveryBinding.bind(view)
        setAdaper()
        cakeAdapter.submitList(setList())
    }

    private fun setAdaper() {
        cakeAdapter =
            CakeItemAddAdapter(
                appExecutors,
                object : CakeItemAddAdapter.ImageAdapterClickLister {
                    override fun onIncrementItem(view: View) {
                    }

                    override fun onDecrementItem(view: View) {
                    }

                })

        binding.list.apply {
            adapter = cakeAdapter
        }
    }

    private fun setList(): ArrayList<CakeItem> {
        val list = ArrayList<CakeItem>()
        list.add(
            CakeItem(
                "0",
                "Mansa Stationaries Classic",
                "500 sheet paper rim",
                "MRP 250$",
                "1240$",
                "",
                true
            )
        )
        list.add(
            CakeItem(
                "0",
                "Mansa Stationaries JK Paper",
                "400 sheet paper rim",
                "MRP 3350$",
                "2240$",
                "",
                true
            )
        )
        list.add(
            CakeItem(
                "0",
                "Mansa Stationaries MEAD",
                "200 sheet paper rim",
                "MRP 230$",
                "2240$",
                "",
                false
            )
        )
        list.add(
            CakeItem(
                "0",
                "Mansa Stationaries Brand name",
                "100 sheet paper rim",
                "MRP 350$",
                "2240$",
                "",
                false
            )
        )
        list.add(
            CakeItem(
                "0",
                "Mansa Stationaries Exclusive",
                "1500 sheet paper rim",
                "MRP 3250$",
                "2240$",
                "",
                false
            )
        )
        list.add(
            CakeItem(
                "0",
                "Mansa Stationaries Classic",
                "300 sheet paper rim",
                "MRP 3250$",
                "2240$",
                "",
                true
            )
        )
        list.add(
            CakeItem(
                "0",
                "Mansa Stationaries Classic",
                "200 sheet paper rim",
                "MRP 3250$",
                "2240$",
                "",
                false
            )
        )
        list.add(
            CakeItem(
                "0",
                "Mansa Stationaries Classic",
                "500 sheet paper rim",
                "MRP 3250$",
                "2240$",
                "",
                false
            )
        )
        list.add(
            CakeItem(
                "0",
                "Mansa Stationaries Classic",
                "15 sheet paper rim",
                "MRP 3250$",
                "2240$",
                ""
                , false
            )
        )
        return list
    }

}