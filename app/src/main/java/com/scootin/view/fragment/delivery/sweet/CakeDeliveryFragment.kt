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
                "Mansa Bakery",
                "Chocolate Cake",
                "MRP 250$",
                "1240$",
                "",
                true
            )
        )
        list.add(
            CakeItem(
                "0",
                "HM Bakery",
                "Vanilla Cake",
                "MRP 3350$",
                "2240$",
                "",
                true
            )
        )
        list.add(
            CakeItem(
                "0",
                "Fresh Bakery",
                "Chocolate Cake",
                "MRP 230$",
                "2240$",
                "",
                false
            )
        )
        list.add(
            CakeItem(
                "0",
                "Fresh Bakery",
                "Chocolate Cake",
                "MRP 350$",
                "2240$",
                "",
                false
            )
        )
        list.add(
            CakeItem(
                "0",
                "Fresh Bakery",
                "Chocolate Cake",
                "MRP 3250$",
                "2240$",
                "",
                false
            )
        )
        list.add(
            CakeItem(
                "0",
                "Fresh Bakery",
                "Chocolate Cake",
                "MRP 3250$",
                "2240$",
                "",
                true
            )
        )
        list.add(
            CakeItem(
                "0",
                "Fresh Bakery",
                "Chocolate Cake",
                "MRP 3250$",
                "2240$",
                "",
                false
            )
        )
        list.add(
            CakeItem(
                "0",
                "Fresh Bakery",
                "Chocolate Cake",
                "MRP 3250$",
                "2240$",
                "",
                false
            )
        )
        list.add(
            CakeItem(
                "0",
                "Fresh Bakery",
                "Chocolate Cake",
                "MRP 3250$",
                "2240$",
                ""
                , false
            )
        )
        return list
    }

}