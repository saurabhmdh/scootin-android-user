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

    var initialOffset: Int = 0
    var count: Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductSearchVO> {
        return try {

            val offset = params.key ?: initialOffset

            val alreadyLoaded = offset * params.loadSize

            val loadsize = if (count != 0 && (alreadyLoaded + params.loadSize) > count) {
                count - alreadyLoaded
            } else {
                params.loadSize
            }
            Timber.i("offset $offset alrerady loaded $alreadyLoaded loadsize = ${loadsize}" )
            Timber.i("shopId $shopId requestSearch $requestSearch")

            val response: Response<List<SearchProductsByCategoryResponse>> = apiService.findProductFromShopWithCategoryAndSubCategory(shopId, requestSearch, offset, loadsize)
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
            Timber.i("Saurabh ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductSearchVO>) = state.anchorPosition
}