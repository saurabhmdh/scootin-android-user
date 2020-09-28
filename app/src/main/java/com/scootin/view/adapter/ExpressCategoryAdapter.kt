package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.R
import com.scootin.databinding.AdapterItemAddEssentialgroceryStoreBinding
import com.scootin.databinding.AdapterItemExpressCategoryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.network.response.sweets.SweetsStore
import timber.log.Timber

class ExpressCategoryAdapter (
    val appExecutors: AppExecutors
): DataBoundListAdapter<HomeResponseCategory, AdapterItemExpressCategoryBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<HomeResponseCategory>(){
        override fun areItemsTheSame(oldItem: HomeResponseCategory, newItem: HomeResponseCategory): Boolean {
            return oldItem.id==newItem.id
        }
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: HomeResponseCategory, newItem: HomeResponseCategory): Boolean {
            return oldItem.id==newItem.id
        }

    }
) {
    override fun createBinding(parent: ViewGroup): AdapterItemExpressCategoryBinding =
        AdapterItemExpressCategoryBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )


    override fun bind(
        binding: AdapterItemExpressCategoryBinding,
        item: HomeResponseCategory,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.categoryRadioButton.text=item.name
        }


    }
    interface StoreImageAdapterClickListener{
        fun onSelectButtonSelected(view: View)
    }
}