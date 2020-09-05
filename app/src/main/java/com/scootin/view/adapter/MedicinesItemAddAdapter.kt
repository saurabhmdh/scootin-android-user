package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemAddMedicinesBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.medicines.MedicinesItem
import timber.log.Timber

class MedicinesItemAddAdapter(
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister
) : DataBoundListAdapter<MedicinesItem, AdapterItemAddMedicinesBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<MedicinesItem>() {
        override fun areItemsTheSame(
            oldItem: MedicinesItem,
            newItem: MedicinesItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: MedicinesItem,
            newItem: MedicinesItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterItemAddMedicinesBinding =
        AdapterItemAddMedicinesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterItemAddMedicinesBinding,
        item: MedicinesItem,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.name.setText(name)
            binding.distance.setText(distance)
            binding.ratingbar.rating = rating.toFloat()
            if (isOpen) binding.onlinestatusStore.setText("Online") else binding.onlinestatusStore.setText("Offline")
        }
    }

    interface ImageAdapterClickLister {
        fun onIncrementItem(view: View)
        fun onDecrementItem(view: View)
    }
}