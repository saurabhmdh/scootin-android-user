package com.scootin.view.fragment.delivery.sweet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentSnacksDeliveryBinding
import com.scootin.databinding.FragmentStationaryDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.stationary.StationaryItem
import com.scootin.network.response.sweets.SnackItem
import com.scootin.network.response.sweets.SweetsItem
import com.scootin.network.response.sweets.SweetsStore
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.SnacksItemAddAdapter
import com.scootin.view.adapter.SnacksStoreAdapter
import com.scootin.view.adapter.StationaryItemAddAdapter
import com.scootin.view.adapter.SweetsStoreAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SnacksDeliveryFragment : Fragment(R.layout.fragment_snacks_delivery) {
    private var binding by autoCleared<FragmentSnacksDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var snacksItemAddAdapter: SnacksItemAddAdapter
    private lateinit var snacksStoreAdapter: SnacksStoreAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSnacksDeliveryBinding.bind(view)
        setAdapter()
        snacksItemAddAdapter.submitList(setList())
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            when (optionId) {
                R.id.materialRadioButton -> {
                    setAdapter()
                    snacksItemAddAdapter.submitList(setList())
                }
                R.id.materialRadioButton2 -> {
                    setStoreAdapter()
                    snacksStoreAdapter.submitList(setStoreList())
                }
            }
        }
    }

    private fun setAdapter() {
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
    private fun setStoreAdapter() {
        snacksStoreAdapter =
            SnacksStoreAdapter(
                appExecutors,
                object : SnacksStoreAdapter.StoreImageAdapterClickListener {

                    override fun onSelectButtonSelected(view: View) {
                    }

                })

        binding.list.apply {
            adapter = snacksStoreAdapter
        }
    }

    private fun setList(): ArrayList<SnackItem> {
        val list = ArrayList<SnackItem>()
        list.add(
            SnackItem(
                "0",
                "Business Name",
                "Dhokla",
                "Rs 1240",
                ""
            )
        )
        list.add(
            SnackItem(
                "0",
                "Business Name",
                "Samosa",
                "Rs 1240",
                ""
            )
        )
        list.add(
            SnackItem(
                "0",
                "Business Name",
                "Bread Pakoda",
                "Rs 1240",
                ""
            )
        )
        list.add(
            SnackItem(
                "0",
                "Business Name",
                "Dhokla",
                "Rs 1240",
                ""
            )
        )
        list.add(
            SnackItem(
                "0",
                "Business Name",
                "Dhokla",
                "Rs 1240",
                ""
            )
        )
        list.add(
            SnackItem(
                "0",
                "Business Name",
                "Dhokla",
                "Rs 1240",
                ""
            )
        )
        list.add(
            SnackItem(
                "0",
                "Business Name",
                "Dhokla",
                "Rs 1240",
                ""
            )
        )
        list.add(
            SnackItem(
                "0",
                "Business Name",
                "Dhokla",
                "Rs 1240",
                ""
            )
        )
        list.add(
            SnackItem(
                "0",
                "Business Name",
                "Dhokla",
                "Rs 1240",
                ""
            )
        )
        list.add(
            SnackItem(
                "0",
                "Business Name",
                "Dhokla",
                "Rs 1240",
                ""
            )
        )
        list.add(
            SnackItem(
                "0",
                "Business Name",
                "Dhokla",
                "Rs 1240",
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