package com.scootin.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StateDetails(
    val countryDetails: CountryDetails,
    val id: Int,
    val name: String
): Parcelable