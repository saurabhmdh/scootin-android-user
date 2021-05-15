package com.scootin.pages

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.scootin.network.api.APIService
import com.scootin.network.request.RequestSearchWithCategoryAndSubCategory
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.view.vo.ProductSearchVO
import retrofit2.Response
import timber.log.Timber

class SearchProductByShop (
    private val apiService: APIService,
    private val shopId: Long,
    private val requestSearch: RequestSearchWithCategoryAndSubCategory
) : PagingSource<Int, ProductSearchVO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductSearchVO> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response: Response<List<SearchProductsByCategoryResponse>> = apiService.findProductFromShopWithCategoryAndSubCategory(shopId, requestSearch, nextPageNumber, params.loadSize)
            val data = response.body()
            if (response.isSuccessful && data != null) {
                val totalPages = response.headers().get("x-total-pages")?.toInt() ?: 0
                val productList = mutableListOf<ProductSearchVO>().apply {
                    data.forEach { product ->
                        add(ProductSearchVO(product))
                    }
                }
                val nextOffset = if (totalPages == nextPageNumber) {
                    null
                } else {
                    nextPageNumber + 1
                }
                LoadResult.Page(
                    data = productList,
                    prevKey = null,
                    nextKey = nextOffset
                )
            } else {
                LoadResult.Error(RuntimeException("${response.code()} ${response.errorBody()}"))
            }
        } catch (e: Exception) {
            Timber.i("Saurabh ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductSearchVO>) = state.anchorPosition
}