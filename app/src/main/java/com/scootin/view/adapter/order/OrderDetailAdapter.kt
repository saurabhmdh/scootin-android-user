package com.scootin.view.adapter.order

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemOrderDetailBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.inorder.OrderInventoryDetails
import com.scootin.view.adapter.DataBoundListAdapter


class OrderDetailAdapter(
    val appExecutors: AppExecutors
) : DataBoundListAdapter<OrderInventoryDetails, AdapterItemOrderDetailBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<OrderInventoryDetails>() {
        override fun areItemsTheSame(
            oldItem: OrderInventoryDetails,
            newItem: OrderInventoryDetails
        ) = oldItem.id == newItem.id


        override fun areContentsTheSame(
            oldItem: OrderInventoryDetails,
            newItem: OrderInventoryDetails
        ) = oldItem == newItem
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterItemOrderDetailBinding {
        val binding = AdapterItemOrderDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return binding
    }

    override fun bind(
        binding: AdapterItemOrderDetailBinding,
        item: OrderInventoryDetails,
        position: Int,
        isLast: Boolean
    ) {

        item.apply {
            binding.itemName.text = item.inventoryDetails.title + " ("+ item.inventoryDetails.description +")"
            binding.quantity.text = "X "+item.quantity.toString()
            binding.itemCost.text = item.totalAmount.toString()
        }
    }
}