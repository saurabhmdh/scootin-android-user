package com.scootin.view.adapter.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.scootin.R
import com.scootin.network.AppExecutors


class SearchitemAdapter(
    val appExecutors: AppExecutors, val listener : OnItemClickListener
) : RecyclerView.Adapter<SearchitemAdapter.SearchViewHolder>() {

    val list = ArrayList<String>()

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val editTextView = itemView.findViewById<AppCompatEditText>(R.id.appCompatEditText2)
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
        viewHolder.editTextView.setText(list.get(position))
        viewHolder.incrementTV.setOnClickListener {
            viewHolder.count.text = "${ viewHolder.count.text.toString().toInt().inc()}"
            listener.onIncrement(viewHolder.count.text.toString())
        }
        viewHolder.decrementTV.setOnClickListener {
            if(viewHolder.count.text.toString().toInt() > 0) {
                viewHolder.count.text = "${viewHolder.count.text.toString().toInt().dec()}"
                listener.onDecrement(viewHolder.count.text.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addList(item: String) {
        list.add(item)
        if(list.isEmpty().not())
        notifyItemInserted(list.size-1)
    }

    interface OnItemClickListener{
        fun onIncrement(count: String)
        fun onDecrement(count : String)
    }

}