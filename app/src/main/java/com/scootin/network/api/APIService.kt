package com.scootin.network.api

import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.*
import com.scootin.network.response.*
import com.scootin.network.response.cart.CartListResponseItem
import com.scootin.network.response.cart.UserCartResponse
import com.scootin.network.response.citywide.CityWideOrderResponse
import com.scootin.network.response.home.DealResponse
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.network.response.home.ResponseServiceArea
import com.scootin.network.response.inorder.InOrderDetail
import com.scootin.network.response.inorder.MultipleOrdersDetails
import com.scootin.network.response.login.ResponseAddAddressSuccess
import com.scootin.network.response.login.ResponseUser
import com.scootin.network.response.order.CheckOutResponse
import com.scootin.network.response.order.OrderHistoryItem
import com.scootin.network.response.orderdetail.OrderDetail
import com.scootin.network.response.orders.DirectOrderResponse
import com.scootin.network.response.placeOrder.PlaceOrderResponse
import com.scootin.network.response.wallet.AddWalletResponse
import com.scootin.network.response.wallet.WalletTransactionResponse
import com.scootin.view.vo.ServiceArea
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

    @POST("user/add")
    suspend fun registerUser(
        @Body requestRegisterUser: RequestRegisterUser,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseUser>


    @GET("category/get-all-shop-category")
    suspend fun getHomeCategory(
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<HomeResponseCategory>>

    @GET("/category/get-all-active-shop-category")
    suspend fun getActiveShopCategory(
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<HomeResponseCategory>>



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

    @GET("/service-area/get-all")
    suspend fun getAllServiceArea(
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<ServiceArea>>


    @POST("/search/{serviceAreaId}/{categoryId}/get-all-shops-by-subcategory")
    suspend fun findShopsBySubCategory(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("categoryId") categoryId: String,
        @Body requestSearch: RequestSearch,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<SearchShopsByCategoryResponse>>

    @POST("/search/{serviceAreaId}/{categoryId}/get-all-shops")
    suspend fun findShops(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("categoryId") categoryId: String,
        @Body requestSearch: RequestSearch,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<SearchShopsByCategoryResponse>>


    @POST("/search/{shopId}/get-all-shop-products")
    suspend fun findProductFromShopWithCategoryAndSubCategory(
        @Path("shopId") shopId: Long,
        @Body requestSearch: RequestSearchWithCategoryAndSubCategory,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<SearchProductsByCategoryResponse>>


    @POST("/search/{serviceAreaId}/{subCategoryId}/get-all-products-with-subcategory")
    suspend fun searchProductBySubCategories(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("subCategoryId") subCategoryId: String,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @Body requestSearch: RequestSearch,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<SearchProductsByCategoryResponse>>


    @POST("notification/user/{id}/update-fcm")
    suspend fun updateFCMID(
        @Path("id") userMobileNumber: String,
        @Body requestFCM: RequestFCM,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseUser>

    @DELETE("/cart/clear-cart/{userId}")
    suspend fun deleteCart(
        @Path("userId") userId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>

    @POST("/cart/update-cart")
    suspend fun updateCart(
        @Body request: AddToCartRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<CartListResponseItem>

    @GET("/cart/get-cart-count/{userId}")
    suspend fun getCartCount(
        @Path("userId") userId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>

    @GET("/cart/get-cart/{userID}")
    suspend fun getUserCartList(
        @Path("userID") userId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<UserCartResponse>

    @POST("/wallet/add-money/{userId}")
    suspend fun addMoney(
        @Path("userId") userId: String,
        @Body addMoneyWallet: AddMoneyWallet,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<AddWalletResponse>

    @GET("/wallet/list-transaction/{userId}")
    suspend fun listTransaction(
        @Path("userId") userId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<WalletTransactionResponse>>

    @POST("/wallet/verifyPayment/{userId}")
    suspend fun verifyWalletPayment(
        @Path("userId") userId: String,
        @Body verify: VerifyAmountRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>

    @GET("/address/update-default-address/{userId}/{addressId}")
    suspend fun updateDefaultAddress(
        @Path("userId") userId: String,
        @Path("addressId") addressId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>

    @DELETE("/address/delete-address/{userId}/{addressId}")
    suspend fun deleteAddress(
        @Path("userId") userId: String,
        @Path("addressId") addressId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<AddressDetails>


    @POST("/address/add-new-address/{userId}")
    suspend fun addNewAddress(
        @Path("userId") userId: String,
        @Body address: AddAddressRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseAddAddressSuccess>

    @GET("/address/get-all-address/{userId}")
    suspend fun getAllAdress(
        @Path("userId") userId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<AddressDetails>>


    @POST("/util/find-distance")
    suspend fun findDistance(
        @Body request: DistanceMeasure,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<DistanceResponse>


    @Multipart
    @POST("/media/upload-image-android")
    suspend fun uploadImage(
        @Part multipartFile: MultipartBody.Part,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<Media>


    @POST("/order/place-order-direct/{userId}")
    suspend fun placeDirectOrder(
        @Path("userId") userId: String,
        @Body request: DirectOrderRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<DirectOrderResponse>

    @POST("/order/checkout-order/{userId}")
    suspend fun checkOutOrder(
        @Path("userId") userId: String,
        @Body request: PromoCodeRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<CheckOutResponse>


    @POST("/order/orders/place-city-wide/{userId}")
    suspend fun placeCityWideOrder(
        @Path("userId") userId: String,
        @Body request: CityWideOrderRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<CityWideOrderResponse>


    @POST("/order/user-confirm-order/{userId}")
    suspend fun userConfirmOrder(
        @Path("userId") userId: String,
        @Body orderRequest: OrderRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<OrderDetail>>


    @POST("/order/user-confirm-order-direct/{orderId}")
    suspend fun userConfirmOrderDirect(
        @Path("orderId") orderId: String,
        @Body orderRequest: OrderRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<DirectOrderResponse>


    @POST("/order/user-confirm-order-citywide/{orderId}")
    suspend fun userConfirmOrderCityWide(
        @Path("orderId") orderId: String,
        @Body orderRequest: OrderRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<CityWideOrderResponse>

    @POST("/payment/payment-verified-citywide")
    suspend fun verifyPaymentCityWide(
        @Body verifyAmountRequest: VerifyAmountRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<CityWideOrderResponse>

    @POST("/payment/apply-promocode/{orderId}/{userId}")
    suspend fun applyPromoCode(
        @Path("orderId") orderId: String,
        @Path("userId") userId: String,
        @Body promoCodeRequest: PromoCodeRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>


    @POST("/payment/payment-verified")
    suspend fun verifyPayment(
        @Body verifyAmountRequest: VerifyAmountRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<PlaceOrderResponse>>


    @GET("/cart/get-cart-price/{userId}")
    suspend fun getTotalPriceFromCart(
        @Path("userId") userId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<Double>

    @GET("/register/get-all-states")
    suspend fun getAllState(
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<State>>

    @GET("/order-history/users/{userId}/get-all")
    suspend fun getAllOrdersForUser(
        @Path("userId") userId: String,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<OrderHistoryItem>>


    @POST("/search/{serviceAreaId}/{categoryId}/get-all-products")
    suspend fun findProductsWithPaging(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("categoryId") categoryId: String,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @Body requestSearch: RequestSearch,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<SearchProductsByCategoryResponse>>


    //Case of sweet & bakery
    @POST("/search/{serviceAreaId}/{subCategoryId}/get-all-products-with-filter")
    suspend fun findProductsWithFilters(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("subCategoryId") subCategoryId: String,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @Body requestSearch: RequestSearchWithFilter,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<SearchProductsByCategoryResponse>>


    @GET("/order/orders/get-direct-order/{id}")
    suspend fun getDirectOrder(
        @Path("id") id: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<OrderDetail>

    @GET("/order/orders/get-city-wide-order/{id}")
    suspend fun getCityWideOrder(
        @Path("id") id: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<CityWideOrderResponse>

    @GET("/order/orders/get-order/{id}")
    suspend fun getOrder(
        @Path("id") id: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<InOrderDetail>

    @POST("/order/orders/get-multiples-order")
    suspend fun getMultipleOrders(
        @Body multipleOrdersRequest: MultipleOrdersRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<MultipleOrdersDetails>

    @GET("/util/slots/{startTime}")
    suspend fun getDeliverySlot(
        @Path("startTime") startTime: Long,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<String>>

    @GET("order/orders/check/{id}")
    suspend fun checkOrder(
        @Path("id") id: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>

    @POST("/order/cancel-order/{orderId}")
    suspend fun userCancelOrder(
        @Path("orderId") orderId: String,
        @Body cancelRequest: CancelOrderRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>


    @POST("/payment/payment-verified-direct")
    suspend fun verifyPaymentDirect(
        @Body verifyAmountRequest: VerifyAmountRequest,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<DirectOrderResponse>


    @POST("/order/user-change-payment/{orderId}")
    suspend fun changePaymentMethod(
        @Path("orderId") orderId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<OrderDetail>


    @POST("/deals/get-all")
    suspend fun getDeals(
        @Body requestDeal: RequestDeals,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<DealResponse>>
}