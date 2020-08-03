package com.scootin.view.fragment.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.scootin.R
import timber.log.Timber


class CategoryDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle("Select Cetegory").setSingleChoiceItems(getListItem(), 0
            ) { dialog, which -> Timber.i("Checked = $which") }
                .setPositiveButton(android.R.string.ok, { dialog, id ->
                    Timber.i("positive = $id")
                })
            .setNegativeButton(android.R.string.cancel, { dialog, id ->
                Timber.i("negative = $id")
                })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getListItem() = requireContext().resources.getStringArray(R.array.express_delivery_options)

}