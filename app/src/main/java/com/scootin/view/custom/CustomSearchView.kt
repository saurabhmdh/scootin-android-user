package com.scootin.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SearchView


class CustomSearchView : SearchView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setOnQueryTextListener(listener: OnQueryTextListener?) {
        super.setOnQueryTextListener(listener)
        val mSearchSrcTextView = this.findViewById<SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)
        mSearchSrcTextView.setOnEditorActionListener { _, _, _ ->
            listener?.onQueryTextSubmit(query.toString())
            true
        }
    }
}