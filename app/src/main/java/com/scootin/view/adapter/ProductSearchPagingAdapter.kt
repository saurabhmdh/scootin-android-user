package com.scootin.view.adapter


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterVegetablesListBinding
import com.scootin.extensions.updateVisibility
import com.scootin.view.holders.DataBoundViewHolder
import com.scootin.view.vo.ProductSearchVO

class ProductSearchPagingAdapter (
    val imageAdapterClickListener: ImageAdapterClickLister
) : PagingDataAdapter<ProductSearchVO, DataBoundViewHolder<AdapterVegetablesListBinding>>(diffCallback = object : DiffUtil.ItemCallback<ProductSearchVO>() {
    override fun areItemsTheSame(oldItem: ProductSearchVO, newItem: ProductSearchVO): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: ProductSearchVO, newItem: ProductSearchVO): Boolean = oldItem == newItem
}) {
    interface ImageAdapterClickLister {
        fun onIncrementItem(view: View, item: ProductSearchVO?, count: Int)
        fun onDecrementItem(view: View, item: ProductSearchVO?, count: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBoundViewHolder<AdapterVegetablesListBinding> {
        val binding = AdapterVegetablesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.price.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
        return DataBoundViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<AdapterVegetablesListBinding>,
        position: Int
    ) {
        val binding = holder.binding
        getItem(position)?.let {item->
            binding.data = item
            //If shop is close, these add item should be hide
            if (item.activeForOrder) {
                binding.parentCount.updateVisibility(true)
            } else {
                binding.parentCount.updateVisibility(false)
            }

            val addVisible = item.displayQuantity == 0

            binding.addItem.updateVisibility(addVisible)
            binding.operation.updateVisibility(addVisible.not())


            binding.addItem.setOnClickListener {
                binding.operation.updateVisibility(true)
                binding.addItem.updateVisibility(false)
                item.displayQuantity +=1
                imageAdapterClickListener.onIncrementItem(it, binding.data, 1)
            }

            binding.increment.setOnClickListener {
                item.displayQuantity +=1
                val number = binding.count.text.toString().toInt()
                binding.count.text = number.inc().toString()
                imageAdapterClickListener.onIncrementItem(it, binding.data, number.inc())
            }

            binding.decrement.setOnClickListener {
                if (item.displayQuantity  > 1) {
                    item.displayQuantity -=1
                    val number = binding.count.text.toString().toInt()
                    if (number > 0) {
                        binding.count.text = number.dec().toString()
                        imageAdapterClickListener.onDecrementItem(it, binding.data, number.dec())
                    }
                } else {
                    item.displayQuantity = 0
                    binding.addItem.updateVisibility(true)
                    binding.operation.updateVisibility(false)
                }

            }
        }

    }
}