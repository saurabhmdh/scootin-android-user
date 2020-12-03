package com.scootin.view.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemAddEssentialGroceryBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.view.holders.DataBoundViewHolder

class ProductSearchPagingAdapter (
    val imageAdapterClickListener: ImageAdapterClickLister
) : PagingDataAdapter<SearchProductsByCategoryResponse, DataBoundViewHolder<AdapterItemAddEssentialGroceryBinding>>(diffCallback = object : DiffUtil.ItemCallback<SearchProductsByCategoryResponse>() {
    override fun areItemsTheSame(oldItem: SearchProductsByCategoryResponse, newItem: SearchProductsByCategoryResponse): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: SearchProductsByCategoryResponse, newItem: SearchProductsByCategoryResponse): Boolean = oldItem == newItem
}) {
//    override fun createBinding(parent: ViewGroup): AdapterItemAddEssentialGroceryBinding {
//        val binding= AdapterItemAddEssentialGroceryBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false
//
//        )
//        binding.price.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
//
//        binding.increment.setOnClickListener {
//            val number = binding.count.text.toString().toInt()
//            binding.count.text = number.inc().toString()
//            imageAdapterClickListener.onIncrementItem(it, binding.data, number.inc())
//        }
//        binding.decrement.setOnClickListener {
//            val number = binding.count.text.toString().toInt()
//            if (number > 0) {
//                binding.count.text = number.dec().toString()
//                imageAdapterClickListener.onDecrementItem(it, binding.data, number.dec())
//            }
//            //Invalid case.. number can't be negative
//        }
//
//        //Need to handle case when its again 0..
//
//
//        binding.operation.updateVisibility(false)
//        binding.addItem.updateVisibility(true)
//
//        binding.addItem.setOnClickListener {
//            binding.operation.updateVisibility(true)
//            binding.addItem.updateVisibility(false)
//            imageAdapterClickListener.onIncrementItem(it, binding.data, 1)
//        }
//
//        return binding
//    }

//    override fun bind(
//        binding: AdapterItemAddEssentialGroceryBinding,
//        item: SearchProductsByCategoryResponse,
//        position: Int,
//        isLast: Boolean
//    ) {
//        binding.data = item
//        //If shop is close, these add item should be hide
//        if (item.shopManagement.shopActiveForOrders) {
//            binding.parentCount.updateVisibility(true)
//        } else {
//            binding.parentCount.updateVisibility(false)
//        }
//
//    }

    interface ImageAdapterClickLister {
        fun onIncrementItem(view: View, item: SearchProductsByCategoryResponse?, count: Int)
        fun onDecrementItem(view: View, item: SearchProductsByCategoryResponse?, count: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBoundViewHolder<AdapterItemAddEssentialGroceryBinding> {
        return DataBoundViewHolder(AdapterItemAddEssentialGroceryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<AdapterItemAddEssentialGroceryBinding>,
        position: Int
    ) {
        val binding = holder.binding
        getItem(position)?.let {item->
            binding.data = item
            //If shop is close, these add item should be hide
            if (item.shopManagement.shopActiveForOrders) {
                binding.parentCount.updateVisibility(true)
            } else {
                binding.parentCount.updateVisibility(false)
            }
        }

    }
}