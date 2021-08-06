package com.scootin.view.vo

import androidx.annotation.Keep

@Keep
enum class GroceryFilters(val value: Int) {
    ALL(0),
    GROCERY(1),
    BREAKFAST(2),
    HOUSEHOLD(3),
    HYGIENE(4);

    companion object {

        fun fromValue(value: Int): GroceryFilters {
            return values().find {
                it.value == value
            } ?: ALL
        }
    }
}