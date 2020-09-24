package com.scootin.view.fragment

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.scootin.R

abstract class BaseFragment : Fragment {
    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    private var dialog: AlertDialog? = null

    fun showLoading(cancellable: Boolean = true, touchCancellable: Boolean = true) {
        activity?.apply {
            val builder = AlertDialog.Builder(this)
            builder.setView(R.layout.view_progress)
            dialog = builder.create().apply {
                setCancelable(cancellable)
                setCanceledOnTouchOutside(touchCancellable)
                show()
            }
        }
    }

    fun dismissLoading() {
        dialog?.apply {
            this.dismiss()
            dialog = null
        }
    }
}