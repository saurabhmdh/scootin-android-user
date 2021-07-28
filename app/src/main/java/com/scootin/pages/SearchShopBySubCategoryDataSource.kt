package com.scootin.pages

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.scootin.network.api.APIService
import com.scootin.network.request.RequestSearchBySabCategories
import com.scootin.network.response.SearchShopsByCategoryResponse
import retrofit2.Response

class SearchShopBySubCategoryDataSource (
    private val apiService: APIService,
    private val serviceAreaId: String,
    private val requestSearch: RequestSearchBySabCategories
) : PagingSource<Int, SearchShopsByCategoryResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchShopsByCategoryResponse> {
        return try {
            val nextPageNumber = params.key ?: 0

            val response: Response<List<SearchShopsByCategoryResponse>> = apiService.findShopsBySubCategory(serviceAreaId, requestSearch, nextPageNumber, params.loadSize)
            val data = response.body()
            if (response.isSuccessful && data != null) {
                val totalPages = response.headers().get("x-total-pages")?.toInt() ?: 0

                val nextOffset = if (totalPages == nextPageNumber) {
                    null
                } else {
                    nextPageNumber + 1
                }

                LoadResult.Page(
                    data = data,
                    prevKey = null,
                    nextKey = nextOffset
                )
            } else {
                LoadResult.Error(RuntimeException("${response.code()} ${response.errorBody()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchShopsByCategoryResponse>)= state.anchorPosition
}