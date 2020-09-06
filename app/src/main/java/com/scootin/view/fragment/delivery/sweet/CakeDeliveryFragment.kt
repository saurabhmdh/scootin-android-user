package com.scootin.view.fragment.delivery.sweet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentCakeDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.sweets.CakeItem
import com.scootin.network.response.sweets.SweetsStore
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.CakeItemAddAdapter
import com.scootin.view.adapter.SweetsStoreAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CakeDeliveryFragment : Fragment(R.layout.fragment_cake_delivery) {
    private var binding by autoCleared<FragmentCakeDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var cakeAdapter: CakeItemAddAdapter
    private lateinit var cakeStoreAdapter: SweetsStoreAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCakeDeliveryBinding.bind(view)
        setAdaper()
        cakeAdapter.submitList(setList())
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            when (optionId) {
                R.id.materialRadioButton -> {
                    setAdaper()
                    cakeAdapter.submitList(setList())
                }
                R.id.materialRadioButton2 -> {
                    setStoreAdapter()
                    cakeStoreAdapter.submitList(setStoreList())
                }
            }
        }

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
    private fun setStoreAdapter() {
        cakeStoreAdapter = SweetsStoreAdapter(
            appExecutors,
            object : SweetsStoreAdapter.StoreImageAdapterClickListener {

                override fun onSelectButtonSelected(view: View) {
                }

            })
        binding.list.apply {
            adapter = cakeStoreAdapter
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