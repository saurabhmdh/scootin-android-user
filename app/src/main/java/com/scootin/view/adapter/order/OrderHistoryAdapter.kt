package com.scootin.view.adapter.order

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.R
import com.scootin.bindings.setDateFromOrderDate
import com.scootin.bindings.setPrice
import com.scootin.databinding.AdapterOrderHistoryListBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.order.OrderHistoryItem
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber
import java.text.DateFormat
import java.util.*

class OrderHistoryAdapter(
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister
) : DataBoundListAdapter<OrderHistoryItem, AdapterOrderHistoryListBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<OrderHistoryItem>() {
        override fun areItemsTheSame(
            oldItem: OrderHistoryItem,
            newItem: OrderHistoryItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: OrderHistoryItem,
            newItem: OrderHistoryItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterOrderHistoryListBinding =
        AdapterOrderHistoryListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterOrderHistoryListBinding,
        item: OrderHistoryItem,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item ${item.id}")
        item.apply {
            binding.orderId.setText(id.toString())
            binding.amount.setPrice(totalAmount)
            binding.date.setDateFromOrderDate(orderDate)
            binding.orderStatus.setText(orderStatus)

            var deliveryType = "Normal"
            if (item.expressDelivery) {
                deliveryType = "Express"
            }

            binding.orderType.setText(deliveryType)

            if (orderStatus == "Ongoing") {
                binding.imgTrack.setImageResource(R.drawable.ic_track_text_button)
                binding.orderStatus.setTextColor(Color.parseColor("#FF834A"))
            } else if (orderStatus == "Delivered") {
                binding.orderStatus.setTextColor(Color.parseColor("#38AA35"))
            } else {
                binding.orderStatus.setTextColor(Color.parseColor("#D10000"))
            }
        }
        binding.imgTrack.setOnClickListener {

            imageAdapterClickListener.onViewDetailsSelected(it, item)
        }
    }

    interface ImageAdapterClickLister {
        fun onViewDetailsSelected(view: View, item: OrderHistoryItem)
    }
}

