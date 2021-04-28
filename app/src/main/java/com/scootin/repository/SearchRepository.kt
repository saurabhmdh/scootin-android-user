package com.scootin.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.scootin.network.api.APIService
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.*
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.network.response.order.OrderHistoryItem
import com.scootin.pages.*
import com.scootin.view.vo.ProductSearchVO
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SearchRepository @Inject constructor(
    private val services: APIService
) {

    fun searchShops(requestSearch: RequestSearch, serviceAreaId: String, categoryId: String): Flow<PagingData<SearchShopsByCategoryResponse>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20)) {
            SearchShopDataSource(services, serviceAreaId, categoryId, requestSearch)
        }.flow
    }

    fun searchShopsBySubCategory(requestSearch: RequestSearch, serviceAreaId: String, categoryId: String): Flow<PagingData<SearchShopsByCategoryResponse>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20)) {
            SearchShopBySubCategoryDataSource(services, serviceAreaId, categoryId, requestSearch)
        }.flow
    }

    suspend fun getUserCartList(userId: String) = services.getUserCartList(userId)

    fun findProductFromShopWithCategoryAndSubCategory(query: String, shopId: Long, categoryId: Long, subCategoryId: Long): Flow<PagingData<ProductSearchVO>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20)) {
            SearchProductByShop(services, shopId, RequestSearchWithCategoryAndSubCategory(query = query, categoryId = categoryId, subCategoryId = subCategoryId))
        }.flow
    }

    suspend fun uploadImage(filePart: MultipartBody.Part) = services.uploadImage(filePart)

    suspend fun getDeliverySlot(currentTime: Long) = services.getDeliverySlot(currentTime)

    fun findProductsWithPaging(query: String, serviceAreaId: String, categoryId: String): Flow<PagingData<ProductSearchVO>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20)) {
            SearchDataSource(services, serviceAreaId, categoryId, RequestSearch(query = query))
        }.flow
    }

    fun searchProductBySubCategories(query: String, serviceAreaId: String, subCategoryId: String): Flow<PagingData<ProductSearchVO>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20)) {
            SearchProductBySubcategory(services, serviceAreaId, subCategoryId, RequestSearch(query = query))
        }.flow
    }


    fun findProductsBySubCategoryWithFilters(serviceAreaId: String, subCategoryId: String, request: RequestSearchWithFilter): Flow<PagingData<ProductSearchVO>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20)) {
            SearchProductBySubcategoryWithFilters(services, serviceAreaId, subCategoryId, request)
        }.flow
    }


}