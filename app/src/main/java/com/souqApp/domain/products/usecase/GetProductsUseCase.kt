package com.souqApp.domain.products.usecase

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.entity.ProductsEntity
import com.souqApp.domain.products.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository,
) {

    suspend fun execute(
        type: Int? = null,
        page: Int? = null,
        search: String? = null,
        tag: Int? = null,
        promo: Int? = null,
        recommended: Int? = null,
    ): Flow<BaseResult<ProductsEntity, WrappedListResponse<ProductEntity>>> {
        return flow {
            val response =
                productsRepository.getProducts(type, page, search, tag, promo, recommended)
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

