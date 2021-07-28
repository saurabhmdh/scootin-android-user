package com.scootin.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.scootin.network.api.APIService
import com.scootin.network.request.RequestSearch
import com.scootin.network.request.RequestSearchBySabCategories
import com.scootin.network.request.RequestSearchWithCategoryAndSubCategory
import com.scootin.network.request.RequestSearchWithFilter
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.pages.*
import com.scootin.view.vo.ProductSearchVO
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SearchRepository @Inject constructor(
    private val services: APIService
) {

    fun searchShops(requestSearch: RequestSearch, serviceAreaId: String, categoryId: String): Flow<PagingData<SearchShopsByCategoryResponse>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20, enablePlaceholders = false)) {
            SearchShopDataSource(services, serviceAreaId, categoryId, requestSearch)
        }.flow
    }

    fun searchShopsBySubCategory(requestSearch: RequestSearchBySabCategories, serviceAreaId: String): Flow<PagingData<SearchShopsByCategoryResponse>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20, enablePlaceholders = false)) {
            SearchShopBySubCategoryDataSource(services, serviceAreaId, requestSearch)
        }.flow
    }

    suspend fun getUserCartList(userId: String) = services.getUserCartList(userId)

    fun findProductFromShopWithCategoryAndSubCategory(query: String, shopId: Long, categoryId: Long, subCategoryId: Long): Flow<PagingData<ProductSearchVO>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20, enablePlaceholders = false)) {
            SearchProductByShop(services, shopId, RequestSearchWithCategoryAndSubCategory(query = query, categoryId = categoryId, subCategoryId = subCategoryId))
        }.flow
    }

    suspend fun uploadImage(filePart: MultipartBody.Part) = services.uploadImage(filePart)

    suspend fun getDeliverySlot(currentTime: Long) = services.getDeliverySlot(currentTime)

    fun findProductsWithPaging(query: String, serviceAreaId: String, categoryId: String): Flow<PagingData<ProductSearchVO>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20, enablePlaceholders = false)) {
            SearchDataSource(services, serviceAreaId, categoryId, RequestSearch(query = query))
        }.flow
    }

    fun searchProductBySubCategories(requestSearch: RequestSearchBySabCategories, serviceAreaId: String): Flow<PagingData<ProductSearchVO>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20, enablePlaceholders = false)) {
            SearchProductBySubcategory(services, serviceAreaId, requestSearch)
        }.flow
    }


    fun findProductsBySubCategoryWithFilters(serviceAreaId: String, subCategoryId: String, request: RequestSearchWithFilter): Flow<PagingData<ProductSearchVO>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20, enablePlaceholders = false)) {
            SearchProductBySubcategoryWithFilters(services, serviceAreaId, subCategoryId, request)
        }.flow
    }


}