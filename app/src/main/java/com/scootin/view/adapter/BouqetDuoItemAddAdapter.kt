package com.scootin.view.adapter

import android.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemAddBouqetDuoBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.sweets.CakeItem
import timber.log.Timber

class BouqetDuoItemAddAdapter(
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister
) : DataBoundListAdapter<CakeItem, AdapterItemAddBouqetDuoBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<CakeItem>() {
        override fun areItemsTheSame(
            oldItem: CakeItem,
            newItem: CakeItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: CakeItem,
            newItem: CakeItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterItemAddBouqetDuoBinding =
        AdapterItemAddBouqetDuoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterItemAddBouqetDuoBinding,
        item: CakeItem,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.name.setText(name)
            binding.price.setText(price)
        }

        val items = arrayOf("1 pounds", "2 pounds", "3 pounds")
        val adapter = ArrayAdapter<String>(
            binding.name.context,
            R.layout.simple_spinner_dropdown_item,
            items
        )
        binding.spinner.setAdapter(adapter)
    }

    interface ImageAdapterClickLister {
        fun onItemSelected(view: View)
    }
}