package com.scootin.view.adapter.order

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterOrderSummaryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.orders.OrderedItemsList
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class OrderSummaryAdapter (
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister
) : DataBoundListAdapter<OrderedItemsList, AdapterOrderSummaryBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<OrderedItemsList>() {
        override fun areItemsTheSame(
            oldItem: OrderedItemsList,
            newItem: OrderedItemsList
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: OrderedItemsList,
            newItem: OrderedItemsList
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
        item: OrderedItemsList,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
           binding.itemName.setText(itemName)
            binding.itemCost.setText(itemCost)
        }

    }

    interface ImageAdapterClickLister {
        fun onViewDetailsSelected(view: View)
    }
}

