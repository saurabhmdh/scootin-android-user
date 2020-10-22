package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterItemTransactionBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.wallet.WalletTransactionResponse


class WalletAdapter(
    val appExecutors: AppExecutors
) : DataBoundListAdapter<WalletTransactionResponse, AdapterItemTransactionBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<WalletTransactionResponse>() {
        override fun areItemsTheSame(
            oldItem: WalletTransactionResponse,
            newItem: WalletTransactionResponse
        ) = oldItem.id == newItem.id


        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: WalletTransactionResponse,
            newItem: WalletTransactionResponse
        ) = oldItem == newItem
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterItemTransactionBinding {
        val binding = AdapterItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false

        )
        return binding
    }

    override fun bind(
        binding: AdapterItemTransactionBinding,
        item: WalletTransactionResponse,
        position: Int,
        isLast: Boolean
    ) {
        binding.name.text =
            "${item.transactionDate} ${item.transactionDescription} ${item.transactionReference} ${item.transactionStatus}"
    }
}