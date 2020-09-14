package com.scootin.view.adapter

import com.scootin.R
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
            binding.price.setText(price)
            binding.detail.setText(detail)
        }

        val items = arrayOf("10 Flowers", "15 Flowers", "20 Flowers")
        val adapter = ArrayAdapter<String>(
            binding.count.context,
            R.layout.bouquet_spinner_layout,

            items
        )
        binding.spinner.setAdapter(adapter)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
    }

    interface ImageAdapterClickLister {
        fun onItemSelected(view: View)
    }
}