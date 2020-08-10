package com.scootin.util.constants

import android.Manifest

object AppConstants {
    const val APPLICATION_BASE_URL = "https://rocky-escarpment-77059.herokuapp.com/"
    const val TIMEOUT_SECOND = 60

    /* Constant value for Write Review Screen */
    const val RESULT_LOAD_IMAGE_VIDEO = 1

    const val UPLOAD_IMAGE_SIZE = 10 * 1024 * 1024 // 10 MB

    var PERMISSION_READ_WRITE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    const val RC_READ_WRITE_PERMISSION = 0x02

}