package com.scootin.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.scootin.R
import com.scootin.databinding.AdapterItemTransactionBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.wallet.WalletTransactionResponse
import java.math.BigDecimal
import java.text.Format
import java.text.NumberFormat
import java.util.*


class WalletAdapter(
    val appExecutors: AppExecutors
) : DataBoundListAdapter<WalletTransactionResponse, AdapterItemTransactionBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<WalletTransactionResponse>() {
        override fun areItemsTheSame(
            oldItem: WalletTransactionResponse,
            newItem: WalletTransactionResponse
        ) = oldItem.id == newItem.id


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
        if (position%2 ==0) {
            binding.name.background = ContextCompat.getDrawable(binding.name.context, R.drawable.ic_grey_background_bar)
        } else {
            binding.name.background = null
        }

        val format: Format = NumberFormat.getCurrencyInstance(Locale("en", "in"))
        val finalValue = format.format(BigDecimal(item.amount))

        val displayText = "$finalValue ${item.paymentType} on ${item.transactionDate}"

        binding.name.text = displayText
    }
}