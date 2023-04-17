package com.souqApp.domain.products

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository,
) :
    PagingSource<Int, ProductEntity>() {

    val request: GetProductsRequest = GetProductsRequest()

    suspend fun execute(
        type: Int? = null,
        page: Int? = null,
        search: String? = null,
        tag: Int? = null,
        promo: Int? = null,
        recommended: Int? = null,
    ): Flow<BaseResult<ProductsEntity, WrappedListResponse<ProductEntity>>> {
        return productsRepository.getProducts(type, page, search, tag, promo, recommended)
    }

    override fun getRefreshKey(state: PagingState<Int, ProductEntity>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductEntity> {
        var products = listOf<ProductEntity>()
        var pageNumber = params.key ?: 1

        execute(
            type = request.categoryId,
            page = pageNumber,
            search = request.search,
            tag = request.tag,
            promo = request.promo,
            recommended = request.recommended
        ).collect {
            products = when (it) {
                is BaseResult.Errors -> emptyList()
                is BaseResult.Success -> {
                    it.data.products
                }
            }
        }

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