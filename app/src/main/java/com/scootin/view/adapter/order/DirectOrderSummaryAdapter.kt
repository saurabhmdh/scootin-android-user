package com.scootin.view.adapter.order

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterExtraDataBinding
import com.scootin.databinding.AdapterOrderSummaryBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.response.ExtraDataItem
import com.scootin.network.response.inorder.OrderInventoryDetails
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class DirectOrderSummaryAdapter (
    val appExecutors: AppExecutors
) : DataBoundListAdapter<ExtraDataItem, AdapterOrderSummaryBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<ExtraDataItem>() {
        override fun areItemsTheSame(
            oldItem: ExtraDataItem,
            newItem: ExtraDataItem
        ) = oldItem.name == newItem.name


        override fun areContentsTheSame(
            oldItem: ExtraDataItem,
            newItem: ExtraDataItem
        ) = oldItem == newItem
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterOrderSummaryBinding {
        val binding = AdapterOrderSummaryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return binding
    }

    override fun bind(
        binding: AdapterOrderSummaryBinding,
        item: ExtraDataItem,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        //Get the individual item price
        item.apply {
            Timber.i("item = $item")
            //Get the individual item price
            item.apply {
                binding.itemName.text = item.name
                binding.itemCost.text = "X "+item.count.toString()
                binding.quantity.updateVisibility(false)

            }
        }
    }
}