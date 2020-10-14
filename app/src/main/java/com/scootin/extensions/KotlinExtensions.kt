package com.scootin.extensions

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import java.util.Date

fun View.updateVisibility(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun View.updateInVisibility(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.INVISIBLE
}

fun Int?.orZero(): Int = this ?: 0
fun Int?.orDefault(value: Int): Int = this ?: value

fun Long?.orZero(): Long = this ?: 0L

fun Double?.orDefault(value: Double): Double = this ?: value

fun Date?.orCurrentDate(): Date = this ?: Date()

inline fun <reified T : Fragment> instanceOf(vararg params: Pair<String, Any>) =
    T::class.java.newInstance().apply {
        arguments = bundleOf(*params)
    }

/*
* Bundle can be directly pass while creating instance
* */
inline fun <reified T : Fragment> instanceOf(bundle: Bundle?) = T::class.java.newInstance().apply {
    arguments = bundle
}

inline fun FragmentManager.inTransaction(tag: String?, func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    tag?.let { fragmentTransaction.addToBackStack(tag) }
    fragmentTransaction.commit()
}

fun Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

fun Fragment.setNavigationResult(result: String, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun <T> Fragment.setNavigationResult(key: String = "result", value: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
}

fun <T> Fragment.getNavigationResultLiveData(key: String = "result"): LiveData<T>? {
    return findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData(key)
}

fun <T> Fragment.getNavigationResultOnce(
    owner: LifecycleOwner,
    key: String = "result",
    onChanged: (T) -> Unit
) {
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
        ?.observe(owner) {
            onChanged.invoke(it)
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(key)
        }
}

fun TextView.makeLinks(color: Int, vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }

        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(color), startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun Context.scanForActivity(): AppCompatActivity? {
    return when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.scanForActivity()
        else -> null
    }
}
