package com.scootin.network.request

data class RequestRegisterUser(
    val city: String,
    val dateOfBirth: String,
    val firstName: String,
    val gender: String,
    val mobileNumber: String,
    val id: Long = -1
)
