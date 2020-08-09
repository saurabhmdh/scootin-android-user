package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemAddEdBinding
import com.scootin.network.AppExecutors
import timber.log.Timber


class EDItemAddAdapter(
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister
) : DataBoundListAdapter<String, AdapterItemAddEdBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterItemAddEdBinding =
        AdapterItemAddEdBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterItemAddEdBinding,
        item: String,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        binding.title.setText(item)
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
    }

    interface ImageAdapterClickLister {
        fun onIncrementItem(view: View)
        fun onDecrementItem(view: View)
    }
}