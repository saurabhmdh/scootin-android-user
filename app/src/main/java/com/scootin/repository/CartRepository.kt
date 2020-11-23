package com.scootin.repository

import com.scootin.network.api.APIService
import com.scootin.network.request.AddToCartRequest
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CartRepository @Inject constructor(
    private val services: APIService
) {
    suspend fun updateCart(request: AddToCartRequest) = services.updateCart(request)

    suspend fun deleteCart(userId: String) = services.deleteCart(userId)

    suspend fun getCartCount(userId: String) = services.getCartCount(userId)
}