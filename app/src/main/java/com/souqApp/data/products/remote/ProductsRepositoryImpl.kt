package com.souqApp.data.products.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.ProductsEntity
import com.souqApp.domain.products.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val productsApi: ProductsApi) :
    ProductsRepository {

    override suspend fun getProducts(
        type: Int?,
        page: Int?,
        search: String?,
        tag: Int?,
        promo: Int?,
        recommended: Int?,
    ): Flow<BaseResult<ProductsEntity, WrappedListResponse<ProductEntity>>> {
        return flow {
            val response = productsApi.getProducts(type, page, search, tag, promo, recommended)
            if (response.status) {
                emit(
                    BaseResult.Success(
                        ProductsEntity(
                            products = response.data.orEmpty(),
                            currentPage = response.currentPage ?: 1,
                            totalPages = response.totalPages ?: 1
                        )
                    )
                )
            } else
                emit(BaseResult.Errors(response))
        }
    }

}