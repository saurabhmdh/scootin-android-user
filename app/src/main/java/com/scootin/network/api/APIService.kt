package com.scootin.network.api

import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.*
import com.scootin.network.response.Address
import com.scootin.network.response.Media
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.network.response.State
import com.scootin.network.response.cart.CartListResponseItem
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.network.response.home.ResponseServiceArea
import com.scootin.network.response.login.ResponseUser
import com.scootin.network.response.order.OrderHistoryItem
import com.scootin.network.response.orderdetail.OrderDetail
import com.scootin.network.response.orders.DirectOrderResponse
import com.scootin.network.response.placeOrder.PlaceOrderResponse
import com.scootin.network.response.wallet.AddWalletResponse
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

    @GET("/search/{shopId}/get-all-products")
    suspend fun findProductFromShop(
        @Path("shopId") shopId: Long,
        @Body requestSearch: RequestSearch
    ): Response<List<SearchProductsByCategoryResponse>>

    @POST("notification/user/{id}/update-fcm")
    suspend fun updateFCMID(
        @Path("id") userMobileNumber: String,
        @Body requestFCM: RequestFCM
    ): Response<ResponseUser>


    @POST("/cart/add-to-cart")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<String>


    @GET("/cart/get-cart/{userID}")
    suspend fun getUserCartList(@Path("userID") userId: String): Response<List<CartListResponseItem>>

    @POST("/wallet/add-money/{userId}")
    suspend fun addMoney(@Path("userId") userId: String, @Body addMoneyWallet: AddMoneyWallet): Response<AddWalletResponse>

    @GET("/wallet/list-transaction/{userId}")
    suspend fun listTransaction(@Path("userId") userId: String): Response<List<WalletTransactionResponse>>

    @POST("/wallet/verifyPayment/{userId}")
    suspend fun verifyWalletPayment(@Path("userId") userId: String, @Body verify: VerifyAmountRequest): Response<String>

    @GET("/address/update-default-address/{userId}/{addressId}")
    suspend fun updateDefaultAddress(
        @Path("userID") userId: String,
        @Path("addressId") addressId: String
    ): Response<String>

    @POST("/address/add-new-address")
    suspend fun addNewAddress(/*@Path("userID") userId: String,*/ @Body address: Address): Response<String>

    @GET("/address/get-all-address/{userId}")
    suspend fun getAllAdress(@Path("userID") userId: String): Response<List<Address>>

    @Multipart
    @POST("/media/upload-image")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<Media>


    @POST("/order/place-order-direct/{userId}")
    suspend fun placeDirectOrder(
        @Path("userId") userId: String,
        @Body request: DirectOrderRequest
    ): Response<DirectOrderResponse>


    @POST("/order/place-order/{userId}")
    suspend fun placeOrder(
        @Path("userId") userId: String,
        @Body request: PlaceOrderRequest
    ): Response<PlaceOrderResponse>

    @POST("/order/user-confirm-order/{orderId}/{userId}")
    suspend fun userConfirmOrder(
        @Path("orderId") orderId: String,
        @Path("userId") userId: String,
        @Body orderRequest: OrderRequest
    ): Response<PlaceOrderResponse>

    @POST("/payment/apply-promocode/{orderId}/{userId}")
    suspend fun applyPromoCode(
        @Path("orderId") orderId: String,
        @Path("userId") userId: String,
        @Body promoCodeRequest: PromoCodeRequest
    ): Response<PlaceOrderResponse>


    @POST("/payment/payment-verified")
    suspend fun verifyPayment(
        @Body verifyAmountRequest: VerifyAmountRequest
    ): Response<String>


    @GET("/cart/get-cart-price/{userId}")
    suspend fun getTotalPriceFromCart(
        @Path("userId") userId: String
    ): Response<Double>

    @GET("/register/get-all-states")
    suspend fun getAllState(): Response<List<State>>

    @GET("/order-history/users/{userId}/get-all")
    suspend fun getAllOrdersForUser(@Path("userId") userId: String): Response<List<OrderHistoryItem>>

    @GET("/order/orders/get-direct-order/{id}")
    suspend fun getDirectOrder(@Path("id") id: String): Response<OrderDetail>

}