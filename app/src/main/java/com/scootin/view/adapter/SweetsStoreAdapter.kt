package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemAddSweetsStoreBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.sweets.SweetsStore
import timber.log.Timber

class SweetsStoreAdapter (
    val appExecutors: AppExecutors,
    val imageAdapterClickLister:StoreImageAdapterClickListener
): DataBoundListAdapter<SweetsStore,AdapterItemAddSweetsStoreBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<SweetsStore>(){
        override fun areItemsTheSame(oldItem: SweetsStore, newItem: SweetsStore): Boolean {
            return oldItem.id==newItem.id
        }
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: SweetsStore, newItem: SweetsStore): Boolean {
            return oldItem.id==newItem.id
        }

    }
) {
    override fun createBinding(parent: ViewGroup): AdapterItemAddSweetsStoreBinding =
        AdapterItemAddSweetsStoreBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )


    override fun bind(
        binding: AdapterItemAddSweetsStoreBinding,
        item: SweetsStore,
        position: Int,
        isLast: Boolean
    ) {
       Timber.i("item = $item")
        item.apply {
            binding.name.setText(name)
            binding.distance.setText(distance)
            binding.ratingCount.setRating(rating)
            if (isOpen) binding.onlinestatusStore.setText("Online") else binding.onlinestatusStore.setText("Offline")
        }
        binding.btnSelect.setOnClickListener {
            imageAdapterClickLister.onSelectButtonSelected(it)
        }
    }
    interface StoreImageAdapterClickListener{
        fun onSelectButtonSelected(view: View)
    }
}
