package com.scootin.network.api

import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.*
import com.scootin.network.response.*
import com.scootin.network.response.cart.CartListResponseItem
import com.scootin.network.response.cart.UserCartResponse
import com.scootin.network.response.citywide.CityWideOrderResponse
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.network.response.home.ResponseServiceArea
import com.scootin.network.response.inorder.InOrderDetail
import com.scootin.network.response.login.ResponseAddAddressSuccess
import com.scootin.network.response.login.ResponseUser
import com.scootin.network.response.order.CheckOutResponse
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


    @POST("/search/{serviceAreaId}/{categoryId}/get-all-shops-by-subcategory")
    suspend fun findShopsBySubCategory(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("categoryId") categoryId: String,
        @Body requestSearch: RequestSearch,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10
    ): Response<List<SearchShopsByCategoryResponse>>

    @POST("/search/{serviceAreaId}/{categoryId}/get-all-shops")
    suspend fun findShops(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("categoryId") categoryId: String,
        @Body requestSearch: RequestSearch,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10
    ): Response<List<SearchShopsByCategoryResponse>>


    @POST("/search/{shopId}/get-all-products")
    suspend fun findProductFromShop(
        @Path("shopId") shopId: Long,
        @Body requestSearch: RequestSearch,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10
    ): Response<List<SearchProductsByCategoryResponse>>

    @POST("/search/{serviceAreaId}/{subCategoryId}/get-all-products-with-subcategory")
    suspend fun searchProductBySubCategories(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("subCategoryId") subCategoryId: String,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @Body requestSearch: RequestSearch
    ): Response<List<SearchProductsByCategoryResponse>>



    @POST("notification/user/{id}/update-fcm")
    suspend fun updateFCMID(
        @Path("id") userMobileNumber: String,
        @Body requestFCM: RequestFCM
    ): Response<ResponseUser>

    @DELETE("/cart/clear-cart/{userId}")
    suspend fun deleteCart(@Path("userId") userId: String): Response<String>

    @POST("/cart/update-cart")
    suspend fun updateCart(@Body request: AddToCartRequest): Response<CartListResponseItem>

    @GET("/cart/get-cart-count/{userId}")
    suspend fun getCartCount(@Path("userId") userId: String): Response<String>

    @GET("/cart/get-cart/{userID}")
    suspend fun getUserCartList(@Path("userID") userId: String): Response<UserCartResponse>

    @POST("/wallet/add-money/{userId}")
    suspend fun addMoney(@Path("userId") userId: String, @Body addMoneyWallet: AddMoneyWallet): Response<AddWalletResponse>

    @GET("/wallet/list-transaction/{userId}")
    suspend fun listTransaction(@Path("userId") userId: String): Response<List<WalletTransactionResponse>>

    @POST("/wallet/verifyPayment/{userId}")
    suspend fun verifyWalletPayment(@Path("userId") userId: String, @Body verify: VerifyAmountRequest): Response<String>

    @GET("/address/update-default-address/{userId}/{addressId}")
    suspend fun updateDefaultAddress(
        @Path("userId") userId: String,
        @Path("addressId") addressId: String
    ): Response<String>

    @DELETE("/address/delete-address/{userId}/{addressId}")
    suspend fun deleteAddress(
        @Path("userId") userId: String,
        @Path("addressId") addressId: String
    ): Response<AddressDetails>


    @POST("/address/add-new-address/{userId}")
    suspend fun addNewAddress(@Path("userId") userId: String, @Body address: AddAddressRequest): Response<ResponseAddAddressSuccess>

    @GET("/address/get-all-address/{userId}")
    suspend fun getAllAdress(@Path("userId") userId: String): Response<List<AddressDetails>>


    @POST("/util/find-distance")
    suspend fun findDistance(
        @Body request: DistanceMeasure
    ): Response<DistanceResponse>


    @Multipart
    @POST("/media/upload-image-android")
    suspend fun uploadImage(@Part multipartFile: MultipartBody.Part): Response<Media>


    @POST("/order/place-order-direct/{userId}")
    suspend fun placeDirectOrder(
        @Path("userId") userId: String,
        @Body request: DirectOrderRequest
    ): Response<DirectOrderResponse>

    @POST("/order/checkout-order/{userId}")
    suspend fun checkOutOrder(
        @Path("userId") userId: String,
        @Body request: PromoCodeRequest
    ): Response<CheckOutResponse>


    @POST("/order/orders/place-city-wide/{userId}")
    suspend fun placeCityWideOrder(
        @Path("userId") userId: String,
        @Body request: CityWideOrderRequest
    ): Response<CityWideOrderResponse>


    @POST("/order/user-confirm-order/{userId}")
    suspend fun userConfirmOrder(
        @Path("userId") userId: String,
        @Body orderRequest: OrderRequest
    ): Response<OrderDetail>


    @POST("/order/user-confirm-order-direct/{orderId}")
    suspend fun userConfirmOrderDirect(
        @Path("orderId") orderId: String,
        @Body orderRequest: OrderRequest
    ): Response<DirectOrderResponse>



    @POST("/payment/apply-promocode/{orderId}/{userId}")
    suspend fun applyPromoCode(
        @Path("orderId") orderId: String,
        @Path("userId") userId: String,
        @Body promoCodeRequest: PromoCodeRequest
    ): Response<PlaceOrderResponse>


    @POST("/payment/payment-verified")
    suspend fun verifyPayment(
        @Body verifyAmountRequest: VerifyAmountRequest
    ): Response<PlaceOrderResponse>


    @GET("/cart/get-cart-price/{userId}")
    suspend fun getTotalPriceFromCart(
        @Path("userId") userId: String
    ): Response<Double>

    @GET("/register/get-all-states")
    suspend fun getAllState(): Response<List<State>>

    @GET("/order-history/users/{userId}/get-all")
    suspend fun getAllOrdersForUser(
        @Path("userId") userId: String,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<List<OrderHistoryItem>>


    @POST("/search/{serviceAreaId}/{categoryId}/get-all-products")
    suspend fun findProductsWithPaging(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("categoryId") categoryId: String,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @Body requestSearch: RequestSearch
    ): Response<List<SearchProductsByCategoryResponse>>


    //Case of sweet & bakery
    @POST("/search/{serviceAreaId}/{subCategoryId}/get-all-products-with-filter")
    suspend fun findProductsWithFilters(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("subCategoryId") subCategoryId: String,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @Body requestSearch: RequestSearchWithFilter
    ): Response<List<SearchProductsByCategoryResponse>>



    @GET("/order/orders/get-direct-order/{id}")
    suspend fun getDirectOrder(@Path("id") id: String): Response<OrderDetail>

    @GET("/order/orders/get-city-wide-order/{id}")
    suspend fun getCityWideOrder(@Path("id") id: String): Response<CityWideOrderResponse>

    @GET("/order/orders/get-order/{id}")
    suspend fun getOrder(@Path("id") id: String): Response<InOrderDetail>


    @GET("/util/slots/{startTime}")
    suspend fun getDeliverySlot(@Path("startTime") startTime: Long): Response<List<String>>

    @GET("order/orders/check/{id}")
    suspend fun checkOrder(@Path("id") id: String): Response<String>

    @POST("/order/cancel-order/{orderId}")
    suspend fun userCancelOrder(
        @Path("orderId") orderId: String,
        @Body cancelRequest: CancelOrderRequest
    ): Response<String>


    @POST("/payment/payment-verified-direct")
    suspend fun verifyPaymentDirect(
        @Body verifyAmountRequest: VerifyAmountRequest
    ): Response<DirectOrderResponse>
}