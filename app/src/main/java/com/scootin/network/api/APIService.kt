package com.scootin.network.api

import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.*
import com.scootin.network.response.Address
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.network.response.cart.CartListResponseItem
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.network.response.home.ResponseServiceArea
import com.scootin.network.response.login.ResponseUser
import com.scootin.network.response.wallet.WalletTransactionResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @POST("auth/login/user")
    suspend fun doLogin(
        @Body options: Map<String, String>,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseUser>

    @GET("category/get-all-shop-category")
    suspend fun getHomeCategory(@HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<List<HomeResponseCategory>>

    @POST("auth/generate-otp")
    suspend fun requestOTP(
        @Body options: Map<String, String>,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseBody>

    @POST("/auth/refresh/user")
    suspend fun refreshToken(
        @Body options: Map<String, String>,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseUser>


    @POST("/service-area/find-by-location")
    suspend fun findServiceArea(
        @Body options: Map<String, String>,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseServiceArea>


    @POST("/search/{serviceAreaId}/{categoryId}/get-all-shops")
    suspend fun findShops(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("categoryId") categoryId: String,
        @Body requestSearch: RequestSearch
    ): Response<List<SearchShopsByCategoryResponse>>


    @POST("/search/{serviceAreaId}/{categoryId}/get-all-products")
    suspend fun findProducts(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("categoryId") categoryId: String,
        @Body requestSearch: RequestSearch
    ): Response<List<SearchProductsByCategoryResponse>>


    @POST("notification/user/{id}/update-fcm")
    suspend fun updateFCMID(
        @Path("id") userMobileNumber: String,
        @Body requestFCM: RequestFCM
    ): Response<ResponseUser>

    @GET("/order/capture-payment")
    suspend fun capturePayment(@Body request: CapturePaymentRequest): Response<String>

    @POST("/cart/add-to-cart")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<String>

    @GET("/order/orders/count-total")
    suspend fun countTotal()

    @GET("/order/place-order")
    suspend fun placeOrder(@Body request: PlaceOrderRequest): Response<String>

    @POST("/cart/get-cart/{userID}")
    suspend fun getUserCartList(@Path("userID") userId: String): Response<List<CartListResponseItem>>

    @POST("/wallet/add-money")
    suspend fun addMoney(): Response<String>

    @GET("/wallet/list-transaction")
    suspend fun listTransaction(): Response<List<WalletTransactionResponse>>

    @GET("/address/update-default-address/{userId}/{addressId}")
    suspend fun updateDefaultAddress(@Path("userID") userId: String, @Path("addressId") addressId: String): Response<String>

    @POST("/address/add-new-address")
    suspend fun addNewAddress(/*@Path("userID") userId: String,*/ @Body address: Address): Response<String>

    @GET("/address/get-all-address/{userId}")
    suspend fun getAllAdress(@Path("userID") userId: String): Response<List<Address>>

    @GET("/search/{shopId}/get-all-products")
    suspend fun findProductFromShop(@Path("shopID") shopID: Int): Response<List<SearchProductsByCategoryResponse>>

    @Multipart
    @POST("/media/upload-image")
    suspend fun uploadImage(@Part file: MultipartBody.Part,): Response<String>


}