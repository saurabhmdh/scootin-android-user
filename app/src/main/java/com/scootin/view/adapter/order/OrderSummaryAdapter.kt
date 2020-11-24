package com.scootin.view.adapter.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterOrderSummaryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.inorder.OrderInventoryDetails
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class OrderSummaryAdapter (
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister
) : DataBoundListAdapter<OrderInventoryDetails, AdapterOrderSummaryBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<OrderInventoryDetails>() {
        override fun areItemsTheSame(
            oldItem: OrderInventoryDetails,
            newItem: OrderInventoryDetails
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: OrderInventoryDetails,
            newItem: OrderInventoryDetails
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterOrderSummaryBinding =
        AdapterOrderSummaryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterOrderSummaryBinding,
        item: OrderInventoryDetails,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        //Get the individual item price
        item.apply {
            binding.itemName.text = item.inventoryDetails.title
            binding.quantity.text = "Quantity ("+item.quantity.toString()+")"
            binding.itemCost.text = item.inventoryDetails.price.toString()
        }
    }

    interface ImageAdapterClickLister {
        fun onViewDetailsSelected(view: View)
    }
}

