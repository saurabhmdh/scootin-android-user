package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemAddCardListBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.cart.CartListResponseItem


class AddCartItemAdapter(
    val appExecutors: AppExecutors

) : DataBoundListAdapter<CartListResponseItem, AdapterItemAddCardListBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<CartListResponseItem>() {
        override fun areItemsTheSame(
            oldItem: CartListResponseItem,
            newItem: CartListResponseItem
        ) = oldItem.id == newItem.id


        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: CartListResponseItem,
            newItem: CartListResponseItem
        ) = oldItem == newItem
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterItemAddCardListBinding {
        val binding = AdapterItemAddCardListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false

        )
        binding.discountPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        return binding
    }

    override fun bind(
        binding: AdapterItemAddCardListBinding,
        item: CartListResponseItem,
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