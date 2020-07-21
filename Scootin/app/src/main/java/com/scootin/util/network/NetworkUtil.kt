package com.scootin.util.network

import android.os.Build
import android.os.Looper

object NetworkUtil {
    fun isMainThread(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Looper.getMainLooper().isCurrentThread
        } else {
            Thread.currentThread() === Looper.getMainLooper().thread
        }
    }

    val isEmulator = Build.FINGERPRINT?.contains("generic") == true
}