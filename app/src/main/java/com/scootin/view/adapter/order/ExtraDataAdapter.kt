package com.scootin.view.adapter.order

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterExtraDataBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.ExtraDataItem
import com.scootin.view.adapter.DataBoundListAdapter

class ExtraDataAdapter (
    val appExecutors: AppExecutors
) : DataBoundListAdapter<ExtraDataItem, AdapterExtraDataBinding>(
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
    override fun createBinding(parent: ViewGroup):AdapterExtraDataBinding {
        val binding = AdapterExtraDataBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return binding
    }

    override fun bind(
        binding: AdapterExtraDataBinding,
        item: ExtraDataItem,
        position: Int,
        isLast: Boolean
    ) {
        if (position % 2 == 0) {
            binding.layout.setBackgroundColor(Color.parseColor("#D6DDFF"))
        } else {
            binding.layout.background = null
        }
        binding.data=item
    }
}