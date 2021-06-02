package com.scootin.view.vo

data class ServiceArea(
    val id: Long,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}