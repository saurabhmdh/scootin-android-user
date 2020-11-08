package com.scootin.view.adapter.order

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterExtraDataBinding
import com.scootin.databinding.AdapterItemOrderDetailBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.SearchISuggestiontem
import com.scootin.network.response.inorder.OrderInventoryDetails
import com.scootin.view.adapter.DataBoundListAdapter

class ExtraDataAdapter (
    val appExecutors: AppExecutors
) : DataBoundListAdapter<SearchISuggestiontem, AdapterExtraDataBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<SearchISuggestiontem>() {
        override fun areItemsTheSame(
            oldItem: SearchISuggestiontem,
            newItem: SearchISuggestiontem
        ) = oldItem.id == newItem.id


        override fun areContentsTheSame(
            oldItem: SearchISuggestiontem,
            newItem: SearchISuggestiontem
        ) = oldItem.id == newItem.id
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
        item: SearchISuggestiontem,
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