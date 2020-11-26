package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterAddressBinding
import com.scootin.databinding.AdapterItemAddEssentialGroceryBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.SearchProductsByCategoryResponse

class AddressAdapter (
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister

) : DataBoundListAdapter<AddressDetails, AdapterAddressBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<AddressDetails>() {
        override fun areItemsTheSame(
            oldItem: AddressDetails,
            newItem: AddressDetails
        ) = oldItem.id == newItem.id


        override fun areContentsTheSame(
            oldItem: AddressDetails,
            newItem: AddressDetails
        ) = oldItem == newItem
    }
)
{
    override fun createBinding(parent: ViewGroup): AdapterAddressBinding {
        val binding = AdapterAddressBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return binding
    }

    override fun bind(
        binding: AdapterAddressBinding,
        item: AddressDetails,
        position: Int,
        isLast: Boolean
    ) {
        binding.data = item
        binding.editIcon.setOnClickListener {
                imageAdapterClickListener.onCreateIcon(item)
        }
        binding.deleteIcon.setOnClickListener {
            imageAdapterClickListener.onDeleteIcon(item)
        }
    }

    interface ImageAdapterClickLister {
        fun onCreateIcon(view: AddressDetails)
        fun onDeleteIcon(view: AddressDetails)
    }
}