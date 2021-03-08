package com.scootin.view.adapter.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.scootin.R
import com.scootin.network.AppExecutors
import com.scootin.network.response.ExtraDataItem


class SearchitemAdapter(
    val appExecutors: AppExecutors, val listener : OnItemClickListener
) : RecyclerView.Adapter<SearchitemAdapter.SearchViewHolder>() {

    val list = ArrayList<ExtraDataItem>()

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val editTextView = itemView.findViewById<AppCompatTextView>(R.id.appCompatEditText2)
        val incrementTV = itemView.findViewById<AppCompatImageView>(R.id.increment2)
        val decrementTV = itemView.findViewById<AppCompatImageView>(R.id.decrement2)
        val count = itemView.findViewById<AppCompatTextView>(R.id.count2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_search_add, parent, false)
        val viewHolder = SearchViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: SearchViewHolder, position: Int) {
        val item = list.get(position)
        viewHolder.editTextView.setText(item.name)
        viewHolder.count.text = "${item.count}"

        viewHolder.incrementTV.setOnClickListener {
            item.count = viewHolder.count.text.toString().toInt().inc()
            viewHolder.count.text = "${item.count}"
            listener.onIncrement(item.count.toString())
        }
        viewHolder.decrementTV.setOnClickListener {
            if(viewHolder.count.text.toString().toInt() > 0) {
                item.count = viewHolder.count.text.toString().toInt().dec()
                viewHolder.count.text = "${item.count}"
                listener.onDecrement(item.count.toString())
                if (viewHolder.count.text.toString().toInt() == 0) {
                    removeItem(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addAdd(items: ArrayList<ExtraDataItem>) {
        list.addAll(items)
        notifyDataSetChanged()
    }
    fun addList(item: ExtraDataItem) {
        list.add(item)
        if(list.isEmpty().not())
        notifyItemInserted(list.size-1)
    }

    fun removeItem(position: Int) {
        if (list.isEmpty().not()) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    interface OnItemClickListener{
        fun onIncrement(count: String)
        fun onDecrement(count : String)
    }

}