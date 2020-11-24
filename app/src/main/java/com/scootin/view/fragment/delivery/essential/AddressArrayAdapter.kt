package com.scootin.view.fragment.delivery.essential

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import timber.log.Timber

class AddressArrayAdapter(
    @param:NonNull private val mContext: Context,
    @SuppressLint("SupportAnnotationUsage") @LayoutRes val list: List<Address>?
) :
    ArrayAdapter<Address>(
        mContext, 0,
        list as List<Address>
    ) {
    @NonNull
    override fun getView(
        position: Int,
        @Nullable convertView: View?,
        @NonNull parent: ViewGroup
    ): View {
        var view = convertView
        if (view == null) {
            view =
                LayoutInflater.from(mContext)
                    .inflate(R.layout.simple_list_item_activated_1, parent, false)
        }

        val model = list?.get(position)
        val textView = view?.findViewById<TextView>(R.id.text1)
        model?.let {
            Timber.i("price  pramotion = ${model.addressType}")
            textView?.setText("model.addressType")
        }
        return view!!
    }
}
