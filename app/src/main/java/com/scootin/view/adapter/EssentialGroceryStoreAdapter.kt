package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.R
import com.scootin.databinding.AdapterItemAddEssentialgroceryStoreBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.sweets.SweetsStore
import timber.log.Timber

class EssentialGroceryStoreAdapter (
    val appExecutors: AppExecutors,
    val imageAdapterClickLister: StoreImageAdapterClickListener
): DataBoundListAdapter<SweetsStore, AdapterItemAddEssentialgroceryStoreBinding>(
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
    override fun createBinding(parent: ViewGroup): AdapterItemAddEssentialgroceryStoreBinding =
        AdapterItemAddEssentialgroceryStoreBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )


    override fun bind(
        binding: AdapterItemAddEssentialgroceryStoreBinding,
        item: SweetsStore,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.name.setText(name)
            binding.distance.setText(distance)
            binding.ratingCount.setRating(rating)
            if (isOpen){
                binding.onlinestatusStore.setText("Online")
                binding.btnSelect.setImageResource(R.drawable.ic_select_button_active)
                binding.btnSelect.setOnClickListener {
                    imageAdapterClickLister.onSelectButtonSelected(it)
                }
            }
            else{
                binding.onlinestatusStore.setText("Offline")
                binding.onlinestatusStore.setTextColor(Color.parseColor("#990f02"))
                binding.btnSelect.setImageResource(R.drawable.ic_select_button_inactive)

            }

        }


    }
    interface StoreImageAdapterClickListener{
        fun onSelectButtonSelected(view: View)
    }
}
