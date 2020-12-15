package com.scootin.bindings

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.scootin.R
import com.scootin.network.glide.GlideApp
import com.scootin.network.response.AddressDetails
import com.scootin.util.UtilUIComponent
import java.lang.StringBuilder
import java.math.BigDecimal
import java.text.Format
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("visibleGone")
fun View.visibleGone(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("setImage")
fun ImageView.setImage(url: String?) {
    GlideApp.with(this.context).setDefaultRequestOptions(getDefaultImage())
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun getDefaultImage() = RequestOptions().apply {
    placeholder(R.drawable.ic_placeholder)
    error(R.drawable.ic_placeholder)
}

@BindingAdapter("setPrice")
fun TextView.setPrice(value: Double) {
    val format: Format = NumberFormat.getCurrencyInstance(Locale("en", "in"))
    val finalValue = format.format(BigDecimal(value))
    text = finalValue
}

@BindingAdapter("setDiscountPrice")
fun TextView.setDiscountPrice(value: Double) {
    val format: Format = NumberFormat.getCurrencyInstance(Locale("en", "in"))
    val finalValue = format.format(BigDecimal(value))
    text = finalValue
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("setDateFromOrderDate")
fun TextView.setDateFromOrderDate(orderDate: String?) {
    orderDate?.let {
        val k = orderDate.toLongOrNull()
        if (k != null) {
            val data = Date(k)
            val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
            text = simpleDateFormat.format(data)
        } else {
            text = orderDate.substring(0, 12)
        }
    }
}
@BindingAdapter("setToIntText")
fun TextView.setToIntText(value: Long) {
    text = value.toString()
}

@BindingAdapter("setOneLineAddress")
fun TextView.setOneLineAddress(address: AddressDetails?) {
    if (address == null) return
    text = UtilUIComponent.setOneLineAddress(address)
}
