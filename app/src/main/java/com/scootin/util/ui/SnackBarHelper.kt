package com.scootin.util.ui

import android.graphics.Color
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.scootin.R


fun Fragment.presentSnackBar(
    message: String,
    duration: Int = Snackbar.LENGTH_INDEFINITE
): Snackbar {
    this.requireView().let {
        return Snackbar.make(
            it,
            message,
            duration
        ).apply {

            setAction("Dismiss") {
                dismiss()
            }

            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                .maxLines = 10
            setActionTextColor(Color.RED)
            ContextCompat.getColor(context, R.color.colorPrimary)
            show()
        }
    }
}
