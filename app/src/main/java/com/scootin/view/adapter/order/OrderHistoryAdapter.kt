package com.scootin.view.adapter.order

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.R
import com.scootin.databinding.AdapterOrderHistoryListBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.orders.OrderHistoryList
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class OrderHistoryAdapter (
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister
) : DataBoundListAdapter<OrderHistoryList, AdapterOrderHistoryListBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<OrderHistoryList>() {
        override fun areItemsTheSame(
            oldItem: OrderHistoryList,
            newItem: OrderHistoryList
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: OrderHistoryList,
            newItem: OrderHistoryList
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
        item: OrderHistoryList,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.orderId.setText(orderId)
            binding.amount.setText(amount)
            binding.date.setText(orderDate)
            binding.orderStatus.setText(status)
            binding.orderType.setText(deliveryType)
            if(status=="Ongoing"){
                binding.imgTrack.setImageResource(R.drawable.ic_track_text_button)
                binding.orderStatus.setTextColor(Color.parseColor("#FF834A"))
            }
            else if(status=="Delivered"){
                binding.orderStatus.setTextColor(Color.parseColor("#38AA35"))
            }
            else{
                binding.orderStatus.setTextColor(Color.parseColor("#D10000"))
            }
        }
        binding.imgTrack.setOnClickListener {

            imageAdapterClickListener.onViewDetailsSelected(it)
        }
    }

    interface ImageAdapterClickLister {
        fun onViewDetailsSelected(view: View)
    }
}

