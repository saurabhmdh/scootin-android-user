package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemAddSnacksBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.sweets.SnackItem
import timber.log.Timber


class SnacksItemAddAdapter(
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister
) : DataBoundListAdapter<SnackItem, AdapterItemAddSnacksBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<SnackItem>() {
        override fun areItemsTheSame(
            oldItem: SnackItem,
            newItem: SnackItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: SnackItem,
            newItem: SnackItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterItemAddSnacksBinding =
        AdapterItemAddSnacksBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterItemAddSnacksBinding,
        item: SnackItem,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.name.setText(name)
            binding.detail.setText(detail)
            binding.price.setText(price)
        }

        binding.increment.setOnClickListener {
            val number = binding.count.text.toString().toInt()
            binding.count.text = number.inc().toString()
            imageAdapterClickListener.onIncrementItem(it)
        }
        binding.decrement.setOnClickListener {
            val number = binding.count.text.toString().toInt()
            if (number > 0)
                binding.count.text = number.dec().toString()
            imageAdapterClickListener.onDecrementItem(it)
        }

        val items = arrayOf("500g", "02Pc", "2kg")
        val adapter = ArrayAdapter<String>(
            binding.count.context,
            com.scootin.R.layout.spinner_layout,
            items
        )

        binding.spinner.setAdapter(adapter)
        adapter.setDropDownViewResource(com.scootin.R.layout.support_simple_spinner_dropdown_item)

    }

    interface ImageAdapterClickLister {
        fun onIncrementItem(view: View)
        fun onDecrementItem(view: View)
    }
}