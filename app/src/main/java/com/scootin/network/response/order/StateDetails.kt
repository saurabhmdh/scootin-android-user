package com.scootin.network.response.order

data class StateDetails(
    val countryDetails: CountryDetails,
    val id: Int,
    val name: String
)