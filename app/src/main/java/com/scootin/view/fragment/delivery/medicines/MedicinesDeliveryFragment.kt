package com.scootin.view.fragment.delivery.medicines

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentMedicinesDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.medicines.MedicinesItem
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.MedicinesItemAddAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MedicinesDeliveryFragment : Fragment(R.layout.fragment_medicines_delivery) {
    private var binding by autoCleared<FragmentMedicinesDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var stationaryAdapter: MedicinesItemAddAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMedicinesDeliveryBinding.bind(view)
        setAdaper()
        stationaryAdapter.submitList(setList())
    }

    private fun setAdaper() {
        stationaryAdapter =
            MedicinesItemAddAdapter(
                appExecutors,
                object : MedicinesItemAddAdapter.ImageAdapterClickLister {
                    override fun onIncrementItem(view: View) {
                    }

                    override fun onDecrementItem(view: View) {
                    }

                })

        binding.list.apply {
            adapter = stationaryAdapter
        }
    }

    private fun setList(): ArrayList<MedicinesItem> {
        val list = ArrayList<MedicinesItem>()
        list.add(
            MedicinesItem(
                "0",
                "Mansa Stationaries Classic",
                "500 km",
                "MRP 250$",
                "1240$",
                true,
                3.5
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Mansa Stationaries JK Paper",
                "2 km",
                "MRP 3350$",
                "2240$",
                true,
                4.0
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Mansa Stationaries MEAD",
                "200 km",
                "MRP 230$",
                "2240$",
                true,
                2.0
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Mansa Stationaries Brand name",
                "100 km",
                "MRP 350$",
                "2240$",
                false,
                5.0
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Mansa Stationaries Exclusive",
                "1500 km",
                "MRP 3250$",
                "2240$",
                false,
                2.0
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Mansa Stationaries Classic",
                "300 km",
                "MRP 3250$",
                "2240$",
                false,
                2.0
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Mansa Stationaries Classic",
                "200 km",
                "MRP 3250$",
                "2240$",
                true,
                2.0
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Mansa Stationaries Classic",
                "500 km",
                "MRP 3250$",
                "2240$",
                false,
                3.5
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Mansa Stationaries Classic",
                "15 km",
                "MRP 3250$",
                "2240$",
                true,
                4.5
            )
        )
        return list
    }

}