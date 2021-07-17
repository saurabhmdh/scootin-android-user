package com.scootin.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterPromoOffersBinding
import com.scootin.network.response.home.DealResponse
import com.scootin.view.holders.DataBoundViewHolder

class DealAdapter: PagingDataAdapter<DealResponse, DataBoundViewHolder<AdapterPromoOffersBinding>>(diffCallback = object : DiffUtil.ItemCallback<DealResponse>() {
    override fun areItemsTheSame(oldItem: DealResponse, newItem: DealResponse): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: DealResponse, newItem: DealResponse): Boolean = oldItem == newItem
}) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBoundViewHolder<AdapterPromoOffersBinding> {
        val binding = AdapterPromoOffersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataBoundViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<AdapterPromoOffersBinding>,
        position: Int
    ) {
        val binding = holder.binding
        getItem(position)?.let {item->
            binding.data = item
        }
    }
}