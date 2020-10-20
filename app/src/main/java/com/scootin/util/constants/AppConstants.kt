package com.scootin.util.constants

import android.Manifest

object AppConstants {
    const val APPLICATION_BASE_URL = "https://ferrous-agency-286401.el.r.appspot.com/"
    const val TIMEOUT_SECOND = 60

    var PERMISSION_READ_WRITE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    const val RC_LOCATION_PERMISSIONS = 0x01
    const val RC_READ_WRITE_PERMISSION = 0x02

    const val USER_INFO = "user-data"
    const val AUTHORIZATION = "Authorization"
    const val MAIN_CATEGORY = "main-category"

    const val SERVICE_AREA = "service-area"

    const val FCM_ID = "fcm-id"
    const val CATEGORY_INFO = "category-info"

    /* Constant value for Write Review Screen */
    const val RESULT_LOAD_IMAGE_VIDEO = 1
    const val UPLOAD_IMAGE_SIZE = 10 * 1024 * 1024 // 10 MB

    var PERMISSIONS_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


}