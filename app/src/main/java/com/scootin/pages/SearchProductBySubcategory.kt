package com.scootin.pages

import androidx.paging.PagingSource
import com.scootin.network.api.APIService
import com.scootin.network.request.RequestSearch
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.view.vo.ProductSearchVO
import retrofit2.Response
import timber.log.Timber
import java.lang.RuntimeException

class SearchProductBySubcategory (
    private val apiService: APIService,
    private val serviceAreaId: String,
    private val subCategoryId: String,
    private val requestSearch: RequestSearch
) : PagingSource<Int, ProductSearchVO>() {

    var initialOffset: Int = 0
    var count: Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductSearchVO> {
        return try {

            val offset = params.key ?: initialOffset

            val alreadyLoaded = offset * params.loadSize

            val loadsize = if (count != 0 && alreadyLoaded > count) {
                count - (alreadyLoaded - params.loadSize)
            } else {
                params.loadSize
            }
            Timber.i("offset $offset alrerady loaded $alreadyLoaded loadsize = ${loadsize}" )

            val response: Response<List<SearchProductsByCategoryResponse>> = apiService.searchProductBySubCategories(serviceAreaId, subCategoryId, offset, loadsize, requestSearch)
            val data = response.body()
            if (response.isSuccessful && data != null) {
                count = response.headers().get("x-total-count")?.toInt() ?: 0

                val nextOffset = if (alreadyLoaded >= count) {
                    null
                } else {
                    offset + 1
                }
                //Convert data to VO
                val productList = mutableListOf<ProductSearchVO>().apply {
                    data.forEach { product ->
                        add(ProductSearchVO(product))
                    }
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
            LoadResult.Error(e)
        }
    }
}