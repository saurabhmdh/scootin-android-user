package com.scootin.network.response

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class TempleInfo(
    val address: String,
    val city: String,
    val contactNo: String,
    val detailDescription: String,
    val email: String,
    val id: String,
    val image: String,
    val latitude: String,
    val longitude: String,
    val mainDeity: String,
    val name: String,
    val openTimeFrom: String,
    val openTimeTo: String,
    val shortDescription: String,
    val trustyName: String,
    val website: String,
    val yearBuilt: String,
    val isOpen: String
): Parcelable