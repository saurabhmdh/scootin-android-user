package com.scootin.view.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterTempleItemBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.TempleInfo
import com.scootin.view.adapter.DataBoundListAdapter

class TempleListAdapter (appExecutors: AppExecutors)
    : DataBoundListAdapter<TempleInfo, AdapterTempleItemBinding>(appExecutors, diffCallback = object : DiffUtil.ItemCallback<TempleInfo>() {
        override fun areItemsTheSame(oldItem: TempleInfo, newItem: TempleInfo): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TempleInfo, newItem: TempleInfo): Boolean = oldItem == newItem
}) {

    override fun createBinding(parent: ViewGroup): AdapterTempleItemBinding = AdapterTempleItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false)

    override fun bind(
        binding: AdapterTempleItemBinding,
        item: TempleInfo,
        position: Int,
        isLast: Boolean
    ) {
        binding.data = item
    }

}