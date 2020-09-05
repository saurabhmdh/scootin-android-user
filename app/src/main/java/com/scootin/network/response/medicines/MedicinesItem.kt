package com.scootin.network.response.medicines

data class MedicinesItem(
    val id: String,
    val name: String,
    val distance: String,
    val image: String,
    val isOpen: Boolean,
    val rating: Double
)