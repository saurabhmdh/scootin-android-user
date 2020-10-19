package com.scootin.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.R
import com.scootin.databinding.AdapterItemAddEssentialgroceryStoreBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.SearchShopsByCategoryResponse
import timber.log.Timber


class ShopSearchAdapter (
    val appExecutors: AppExecutors,
    val imageAdapterClickLister: StoreImageAdapterClickListener
): DataBoundListAdapter<SearchShopsByCategoryResponse, AdapterItemAddEssentialgroceryStoreBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<SearchShopsByCategoryResponse>(){
        override fun areItemsTheSame(oldItem: SearchShopsByCategoryResponse, newItem: SearchShopsByCategoryResponse) = oldItem.shopID == newItem.shopID
        override fun areContentsTheSame(oldItem: SearchShopsByCategoryResponse, newItem: SearchShopsByCategoryResponse) = oldItem == newItem

    }
) {
    override fun createBinding(parent: ViewGroup): AdapterItemAddEssentialgroceryStoreBinding =
        AdapterItemAddEssentialgroceryStoreBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )


    override fun bind(
        binding: AdapterItemAddEssentialgroceryStoreBinding,
        item: SearchShopsByCategoryResponse,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {

            //TODO: Its should use databinding..
            binding.name.setText(name)
            binding.distance.setText(distance)

            binding.ratingCount.setRating(rating.toFloat())

//            if (isOpen){
            binding.onlinestatusStore.setText("Online")
            binding.btnSelect.setImageResource(R.drawable.ic_select_button_active)
            binding.btnSelect.setOnClickListener {
                imageAdapterClickLister.onSelectButtonSelected(it)
            }
//            }
//            else{
//                binding.onlinestatusStore.setText("Offline")
//                binding.onlinestatusStore.setTextColor(Color.parseColor("#990f02"))
//                binding.btnSelect.setImageResource(R.drawable.ic_select_button_inactive)
//
//            }

        }


    }
    interface StoreImageAdapterClickListener{
        fun onSelectButtonSelected(view: View)
    }
}
