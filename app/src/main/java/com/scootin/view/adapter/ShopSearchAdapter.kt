package com.scootin.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scootin.R
import com.scootin.databinding.AdapterItemAddEssentialgroceryStoreBinding
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.view.holders.DataBoundViewHolder
import timber.log.Timber


class ShopSearchAdapter (
    val imageAdapterClickLister: StoreImageAdapterClickListener
): PagingDataAdapter<SearchShopsByCategoryResponse, DataBoundViewHolder<AdapterItemAddEssentialgroceryStoreBinding>>(
    diffCallback = object : DiffUtil.ItemCallback<SearchShopsByCategoryResponse>(){
        override fun areItemsTheSame(oldItem: SearchShopsByCategoryResponse, newItem: SearchShopsByCategoryResponse) = oldItem.shopID == newItem.shopID
        override fun areContentsTheSame(oldItem: SearchShopsByCategoryResponse, newItem: SearchShopsByCategoryResponse) = oldItem == newItem

    }
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBoundViewHolder<AdapterItemAddEssentialgroceryStoreBinding> {
        val binding = AdapterItemAddEssentialgroceryStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataBoundViewHolder(binding)
    }
    override fun onBindViewHolder(
        holder: DataBoundViewHolder<AdapterItemAddEssentialgroceryStoreBinding>,
        position: Int
    ) {
        val binding = holder.binding
        getItem(position)?.let { item ->
            Timber.i("item = $item")
            item.apply {
                binding.data = item
                binding.name.setText(name)
                binding.distance.setText(distance)

                binding.ratingCount.setRating(rating.toFloat())

                if (online) {
                    binding.onlinestatusStore.setText("Online")
                    binding.btnSelect.setImageResource(R.drawable.ic_select_button_active)
                    binding.btnSelect.setOnClickListener {
                        imageAdapterClickLister.onSelectButtonSelected(item)
                    }
                } else {
                    binding.onlinestatusStore.setText("Offline")
                    binding.onlinestatusStore.setTextColor(Color.parseColor("#990f02"))
                    binding.btnSelect.setImageResource(R.drawable.ic_select_button_inactive)
                }

            }
        }

    }
    interface StoreImageAdapterClickListener{
        fun onSelectButtonSelected(shopInfo: SearchShopsByCategoryResponse)
    }
}
