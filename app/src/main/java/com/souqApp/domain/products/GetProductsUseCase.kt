package com.souqApp.domain.products

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
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
        return productsRepository.getProducts(type, page, search, tag, promo, recommended)
    }


}

