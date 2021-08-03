package com.scootin.view.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterPromoOffersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.home.DealResponse

//class DealAdapter: PagingDataAdapter<DealResponse, DataBoundViewHolder<AdapterPromoOffersBinding>>(diffCallback = object : DiffUtil.ItemCallback<DealResponse>() {
//    override fun areItemsTheSame(oldItem: DealResponse, newItem: DealResponse): Boolean = oldItem.id == newItem.id
//    override fun areContentsTheSame(oldItem: DealResponse, newItem: DealResponse): Boolean = oldItem == newItem
//}) {
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): DataBoundViewHolder<AdapterPromoOffersBinding> {
//        val binding = AdapterPromoOffersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return DataBoundViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(
//        holder: DataBoundViewHolder<AdapterPromoOffersBinding>,
//        position: Int
//    ) {
//        val binding = holder.binding
//        getItem(position)?.let {item->
//            binding.data = item
//        }
//    }
//}
class DealAdapter(val appExecutors: AppExecutors, val onTouch: OnTouch) :DataBoundListAdapter<DealResponse, AdapterPromoOffersBinding>(appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<DealResponse>() {
        override fun areItemsTheSame(oldItem: DealResponse, newItem: DealResponse) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: DealResponse, newItem: DealResponse) = oldItem == newItem

    }) {
    override fun createBinding(parent: ViewGroup): AdapterPromoOffersBinding = AdapterPromoOffersBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )

    override fun bind(
        binding: AdapterPromoOffersBinding,
        item: DealResponse,
        position: Int,
        isLast: Boolean
    ) {
        binding.data = item
        binding.promoImg.setOnTouchListener({ v, event -> onTouch.onTouch(event) })
    }

    interface OnTouch {
        fun onTouch(event: MotionEvent?): Boolean
    }
}
