package com.souqApp.domain.products.usecase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.products.ProductsRepository

class ProductsPagingSource(
    private val productsRepository: ProductsRepository,
    private val tag: Int?,
    private val categoryId: Int?,
    private val promo: Int?,
    private val search: String?,
    private val recommended: Int?
) : PagingSource<Int, ProductEntity>() {

    override fun getRefreshKey(state: PagingState<Int, ProductEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductEntity> {
        var pageNumber = params.key ?: 1
        val response =
            productsRepository.getProducts(categoryId, pageNumber, search, tag, promo, recommended)
        val products = response.data.orEmpty()

        return LoadResult.Page(
            data = products,
            prevKey = if (pageNumber == 1) null else pageNumber - 1,
            nextKey = if (products.isNotEmpty()) ++pageNumber else null
        )
    }
}

data class GetProductsRequest(
    var categoryId: Int? = null,
    var search: String? = null,
    var tag: Int? = null,
    var promo: Int? = null,
    var recommended: Int? = null,
)


