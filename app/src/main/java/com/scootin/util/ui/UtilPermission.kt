package com.scootin.util.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.scootin.util.constants.AppConstants

object UtilPermission {

    fun hasReadWritePermission(context: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestForReadWritePermission(context: Context) {

        ActivityCompat
            .requestPermissions(
                context as Activity,
                AppConstants.PERMISSION_READ_WRITE,
                AppConstants.RC_READ_WRITE_PERMISSION
            )
    }
}
