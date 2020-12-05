package com.scootin.network.response

data class DistanceResponse(
    val elements: List<Element>
) {
    data class Element(
        val distance: Distance,
        val duration: Duration,
        val status: String
    ) {
        data class Distance(
            val text: String,
            val value: Int
        )

        data class Duration(
            val text: String,
            val value: Int
        )
    }
}