package com.scootin.network.response.login

import androidx.annotation.Keep

@Keep
data class ResponseUser(val id: Int, val user: String, val token: String, val role: ROLE) {

    fun getTokenData(): String {
        if (token.contains("Bearer")) {
            return token.substring(7, token.length)
        }
        return token
    }
}

@Keep
enum class ROLE {
    ADMIN,
    USER,
    SUPPLIER,
    RIDER
}