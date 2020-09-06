package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemAddStationaryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.stationary.StationaryItem
import timber.log.Timber

class StationaryItemAddAdapter(
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister
) : DataBoundListAdapter<StationaryItem, AdapterItemAddStationaryBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<StationaryItem>() {
        override fun areItemsTheSame(
            oldItem: StationaryItem,
            newItem: StationaryItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: StationaryItem,
            newItem: StationaryItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterItemAddStationaryBinding =
        AdapterItemAddStationaryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterItemAddStationaryBinding,
        item: StationaryItem,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.name.setText(name)
            binding.detail.setText(detail)
            binding.price.setText(price)
            binding.shopName.setText(shopName)
            binding.discountPrice.setText(discountPrice)
        }
        binding.discountPrice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG

        binding.increment.setOnClickListener {
            val number = binding.count.text.toString().toInt()
            binding.count.text = number.inc().toString()
            imageAdapterClickListener.onIncrementItem(it)
        }
        binding.decrement.setOnClickListener {
            val number = binding.count.text.toString().toInt()
            if (number > 0)
                binding.count.text = number.dec().toString()
            imageAdapterClickListener.onDecrementItem(it)
        }
    }

    interface ImageAdapterClickLister {
        fun onIncrementItem(view: View)
        fun onDecrementItem(view: View)
    }
}