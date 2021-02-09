package com.scootin.extensions

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import org.json.JSONObject


fun RadioGroup.getCheckedRadioButtonPosition(): Int {
    val radioButtonId = checkedRadioButtonId
    return children.filter { it is RadioButton }
        .mapIndexed { index: Int, view: View ->
            index to view
        }.firstOrNull {
            it.second.id == radioButtonId
        }?.first ?: -1
}

fun getNetworkError(message: String?): String {
    message?.let {
        val obj = JSONObject(message)
        return obj.getString("error")
    } ?: return "Server error"
}