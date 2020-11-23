package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemAddCardListBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.network.response.cart.CartListResponseItem


//Handle add and remove from cart..
class AddCartItemAdapter(
    val appExecutors: AppExecutors,
    val cartItemClickLister: CartItemClickLister
) : DataBoundListAdapter<CartListResponseItem, AdapterItemAddCardListBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<CartListResponseItem>() {
        override fun areItemsTheSame(
            oldItem: CartListResponseItem,
            newItem: CartListResponseItem
        ) = oldItem.id == newItem.id

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

        binding.increment.setOnClickListener {
            val number = binding.count.text.toString().toInt()
            binding.count.text = number.inc().toString()
            cartItemClickLister.onIncrementItem(binding.data, number.inc())
        }

        binding.decrement.setOnClickListener {
            val number = binding.count.text.toString().toInt()
            if (number > 0) {
                binding.count.text = number.dec().toString()
                cartItemClickLister.onDecrementItem(binding.data, number.dec())
            }
        }
        return binding
    }

    override fun bind(
        binding: AdapterItemAddCardListBinding,
        item: CartListResponseItem,
        position: Int,
        isLast: Boolean
    ) {
        binding.data = item
        binding.count.text = item.quantity?.toString()
    }


    interface CartItemClickLister {
        fun onIncrementItem(item: CartListResponseItem?, count: Int)
        fun onDecrementItem(item: CartListResponseItem?, count: Int)
    }
}