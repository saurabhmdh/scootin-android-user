package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemAddEssentialGroceryBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.response.SearchProductsByCategoryResponse


class AddCartItemAdapter (
    val appExecutors: AppExecutors

) : DataBoundListAdapter<SearchProductsByCategoryResponse, AdapterItemAddEssentialGroceryBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<SearchProductsByCategoryResponse>() {
        override fun areItemsTheSame(
            oldItem: SearchProductsByCategoryResponse,
            newItem: SearchProductsByCategoryResponse
        ) = oldItem.id == newItem.id


        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: SearchProductsByCategoryResponse,
            newItem: SearchProductsByCategoryResponse
        ) = oldItem == newItem
    }
)
{
    override fun createBinding(parent: ViewGroup): AdapterItemAddEssentialGroceryBinding {
       val binding= AdapterItemAddEssentialGroceryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false

        )
        binding.discountPrice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG

        binding.operation.updateVisibility(false)
        binding.addItem.updateVisibility(true)

        return binding
    }

    override fun bind(
        binding: AdapterItemAddEssentialGroceryBinding,
        item: SearchProductsByCategoryResponse,
        position: Int,
        isLast: Boolean
    ) {
        binding.data = item



//        val items = arrayOf("500g", "1kg", "2kg")
//        val adapter = ArrayAdapter<String>(
//            binding.count.context,
//            R.layout.spinner_layout_essential,
//            items
//        )
//
//        binding.spinner.setAdapter(adapter)
//        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

    }

}