package com.scootin.util.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

    fun requestForReadWritePermission(context: Activity) {
        ActivityCompat.requestPermissions(
                context,
                AppConstants.PERMISSION_READ_WRITE,
                AppConstants.RC_READ_WRITE_PERMISSION
            )
    }


    fun hasMapPermission(context: Context) = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED


    fun requestMapPermission(fragment: Fragment) {
        fragment.requestPermissions(
            AppConstants.PERMISSIONS_LOCATION,
            AppConstants.RC_LOCATION_PERMISSIONS
        )
    }

    fun hasReadWritePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}
