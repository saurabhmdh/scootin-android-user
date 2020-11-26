package com.scootin.view.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterAddressBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.AddressDetails


class AddressAdapter (
    val appExecutors: AppExecutors,
    val iClickListener: IClickLister

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
            iClickListener.onCreateIcon(item, position)
        }
        binding.deleteIcon.setOnClickListener {
            iClickListener.onDeleteIcon(item, position)
        }
        binding.rootView.setOnClickListener {
            iClickListener.checkboxSelected(item, position)
        }
    }

    interface IClickLister {
        fun onCreateIcon(address: AddressDetails, position: Int)
        fun onDeleteIcon(address: AddressDetails, position: Int)
        fun checkboxSelected(address: AddressDetails, position: Int)
    }
}