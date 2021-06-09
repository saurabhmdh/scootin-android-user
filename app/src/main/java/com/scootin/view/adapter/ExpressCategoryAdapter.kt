package com.scootin.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterExpressCategoryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.home.HomeResponseCategory


class ExpressCategoryAdapter(
    val appExecutors: AppExecutors
) : DataBoundListAdapter<HomeResponseCategory, AdapterExpressCategoryBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<HomeResponseCategory>() {
        override fun areItemsTheSame(
            oldItem: HomeResponseCategory,
            newItem: HomeResponseCategory
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: HomeResponseCategory,
            newItem: HomeResponseCategory
        ) = oldItem == newItem
    }
) {
    
    override fun createBinding(parent: ViewGroup): AdapterExpressCategoryBinding {
        val binding = AdapterExpressCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return binding
    }

    override fun bind(
        binding: AdapterExpressCategoryBinding,
        item: HomeResponseCategory,
        position: Int,
        isLast: Boolean
    ) {
       binding.radioButton.setText(item.name)
    }
}

