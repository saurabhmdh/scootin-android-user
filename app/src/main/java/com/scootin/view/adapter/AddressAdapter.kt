package com.scootin.view.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterAddressBinding
import com.scootin.network.AppExecutors
import com.scootin.view.vo.AddressVo


class AddressAdapter (
    val appExecutors: AppExecutors,
    val iClickListener: IClickLister
) : DataBoundListAdapter<AddressVo, AdapterAddressBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<AddressVo>() {
        override fun areItemsTheSame(
            oldItem: AddressVo,
            newItem: AddressVo
        ) = oldItem == newItem


        override fun areContentsTheSame(
            oldItem: AddressVo,
            newItem: AddressVo
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
        item: AddressVo,
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
        fun onCreateIcon(address: AddressVo, position: Int)
        fun onDeleteIcon(address: AddressVo, position: Int)
        fun checkboxSelected(address: AddressVo, position: Int)
    }
}