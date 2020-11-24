package com.scootin.network.response

import com.scootin.network.response.inorder.CountryDetails

data class StateDetails(
    val countryDetails: CountryDetails,
    val id: Int,
    val name: String
)