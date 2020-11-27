package com.scootin.pages

import androidx.paging.PagingSource
import com.scootin.network.api.APIService
import com.scootin.network.request.RequestHistory
import com.scootin.network.response.order.OrderHistoryItem
import com.scootin.network.response.orders.DirectOrderResponse
import retrofit2.Response
import timber.log.Timber
import java.lang.RuntimeException

class OrderDataSource (
    private val apiService: APIService,
    private val id: String
) : PagingSource<Int, OrderHistoryItem>() {

    var initialOffset: Int = 0
    var count: Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderHistoryItem> {
        return try {

            val offset = params.key ?: initialOffset

            val alreadyLoaded = offset * params.loadSize

            val loadsize = if (count != 0 && alreadyLoaded > count) {
                count - (alreadyLoaded - params.loadSize)
            } else {
                params.loadSize
            }
            Timber.i("offset $offset alrerady loaded $alreadyLoaded loadsize = ${loadsize}" )

            val response: Response<List<OrderHistoryItem>> = apiService.getAllOrdersForUser(id, offset, loadsize)
            val data = response.body()
            if (response.isSuccessful && data != null) {
                count = response.headers().get("x-total-count")?.toInt() ?: 0

                val nextOffset = if (alreadyLoaded >= count) {
                    null
                } else {
                    offset + 1
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
}