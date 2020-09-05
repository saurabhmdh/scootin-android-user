package com.scootin.view.fragment.delivery.sweet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentStationaryDeliveryBinding
import com.scootin.databinding.FragmentSweetsDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.sweets.SweetsItem
import com.scootin.network.response.sweets.SweetsStore
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.SweetsItemAddAdapter
import com.scootin.view.adapter.SweetsStoreAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SweetsDeliveryFragment : Fragment(R.layout.fragment_sweets_delivery) {
    private var binding by autoCleared<FragmentSweetsDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var sweetsAdapter: SweetsItemAddAdapter
    private lateinit var sweetsStoreAdapter: SweetsStoreAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSweetsDeliveryBinding.bind(view)
        setAdaper()
        sweetsAdapter.submitList(setList())
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            when (optionId) {
                R.id.materialRadioButton -> {
                    setAdaper()
                    sweetsAdapter.submitList(setList())
                }
                R.id.materialRadioButton2 -> {
                    setStoreAdapter()
                    sweetsStoreAdapter.submitList(setStoreList())
                }
            }
        }


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

    private fun setStoreAdapter() {
        sweetsStoreAdapter = SweetsStoreAdapter(
            appExecutors,
            object : SweetsStoreAdapter.StoreImageAdapterClickListener {

                override fun onSelectButtonSelected(view: View) {
                }

            })
        binding.list.apply {
            adapter = sweetsStoreAdapter
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

    private fun setStoreList(): ArrayList<SweetsStore> {
        val storelist = ArrayList<SweetsStore>()
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4f,true,"")
        )
        storelist.add(
            SweetsStore("0", "Business Name", "500m", 4.4f,true,"")
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
