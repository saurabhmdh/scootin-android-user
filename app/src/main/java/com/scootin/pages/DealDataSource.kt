package com.scootin.pages

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.scootin.network.api.APIService
import com.scootin.network.request.RequestDeals
import com.scootin.network.response.home.DealResponse
import retrofit2.Response


class DealDataSource (
    private val apiService: APIService,
    private val type: String
) : PagingSource<Int, DealResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DealResponse> {
        return try {
            val nextPageNumber = params.key ?: 0

            val response: Response<List<DealResponse>> = apiService.getDeals(RequestDeals(type), nextPageNumber, params.loadSize)
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

    override fun getRefreshKey(state: PagingState<Int, DealResponse>) = state.anchorPosition
}