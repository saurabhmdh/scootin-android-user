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
                "Raj Pharmacy",
                "500 km",
                "",
                true,
                3.5
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Medic Shop",
                "2 km",
                "",
                false,
                4.0
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Raj Pharmacy",
                "500 km",
                "",
                true,
                3.5
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Medic Shop",
                "2 km",
                "",
                false,
                4.0
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Raj Pharmacy",
                "500 km",
                "",
                true,
                3.5
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Medic Shop",
                "2 km",
                "",
                false,
                4.0
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Raj Pharmacy",
                "500 km",
                "",
                true,
                3.5
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Medic Shop",
                "2 km",
                "",
                false,
                4.0
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Raj Pharmacy",
                "500 km",
                "",
                true,
                3.5
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Medic Shop",
                "2 km",
                "",
                false,
                4.0
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Raj Pharmacy",
                "500 km",
                "",
                true,
                3.5
            )
        )
        list.add(
            MedicinesItem(
                "0",
                "Medic Shop",
                "2 km",
                "",
                false,
                4.0
            )
        )
        return list
    }

}