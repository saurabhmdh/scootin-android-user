package com.scootin.view.vo

import androidx.annotation.Keep

@Keep
enum class SweetFilter(val value: Int) {
    SWEETS(0),
    SNACKS(1),
    CAKE(2);
    companion object {

        fun fromValue(value: Int): SweetFilter {
            return SweetFilter.values().find {
                it.value == value
            } ?: SweetFilter.SWEETS
        }
    }
}