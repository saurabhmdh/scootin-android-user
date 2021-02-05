package com.scootin.view.adapter.order

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scootin.R
import com.scootin.bindings.setPrice
import com.scootin.databinding.AdapterOrderHistoryListBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.order.OrderHistoryItem
import com.scootin.view.holders.DataBoundViewHolder
import timber.log.Timber


class OrderHistoryAdapter(
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister
) : PagingDataAdapter<OrderHistoryItem, DataBoundViewHolder<AdapterOrderHistoryListBinding>>(diffCallback = object : DiffUtil.ItemCallback<OrderHistoryItem>() {
    override fun areItemsTheSame(oldItem: OrderHistoryItem, newItem: OrderHistoryItem): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: OrderHistoryItem, newItem: OrderHistoryItem): Boolean = oldItem == newItem
}) {
    interface ImageAdapterClickLister {
        fun onViewDetailsSelected(view: View, item: OrderHistoryItem)
    }

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<AdapterOrderHistoryListBinding>,
        position: Int
    ) {
        val binding = holder.binding

        getItem(position)?.let { item ->

            Timber.i("item = $item ${item.id}")
            item.apply {

                binding.orderId.setText(id.toString())
                binding.amount.setPrice(totalAmount)
                binding.date.setText(orderDate)

                binding.orderStatus.setText(orderStatus)
                binding.orderType.setText(orderType)

                var deliveryType = "Normal"

                when(item.orderType){
                    "DIRECT"-> {
                        if (item.expressDelivery) {
                            deliveryType = "Express"
                        } else {
                            deliveryType = "Normal"
                        }
                    }
                    "CITYWIDE"->{
                        deliveryType = "Citywide"
                    }
                    "NORMAL"->{
                        deliveryType = "Normal"
                    }
                }


                binding.orderType.setText(deliveryType)

                if (orderStatus == "PLACED" || orderStatus == "UNCONFIRMED" || orderStatus == "PACKED") {
                    binding.imgTrack.setImageResource(R.drawable.ic_track_text_button)
                    binding.orderStatus.setText("Ongoing")
                    binding.orderStatus.setTextColor(Color.parseColor("#FF834A"))
                } else if (orderStatus == "COMPLETED") {
                    binding.orderStatus.setText("Delivered")
                    binding.orderStatus.setTextColor(Color.parseColor("#38AA35"))
                } else if (orderStatus == "CANCELLED") {
                    binding.orderStatus.setText("Cancelled")
                    binding.orderStatus.setTextColor(Color.parseColor("#D10000"))
                }
            }
            binding.rootView.setOnClickListener {
                imageAdapterClickListener.onViewDetailsSelected(it, item)
            }
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBoundViewHolder<AdapterOrderHistoryListBinding> {
        return DataBoundViewHolder(AdapterOrderHistoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }
}

