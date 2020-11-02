package com.scootin.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class State(
    val countryDetails: CountryDetails,
    val id: Int,
    val name: String
) : Parcelable {
    @Parcelize
    data class CountryDetails(
        val id: Int,
        val name: String
    ) : Parcelable
}