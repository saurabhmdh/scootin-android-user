package com.scootin.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class State(
    val countryDetails: CountryDetails,
    val id: Int,
    val name: String
) : Parcelable {

    override fun toString(): String {
        return name
    }
    @Parcelize
    data class CountryDetails(
        val id: Int,
        val name: String
    ) : Parcelable
}