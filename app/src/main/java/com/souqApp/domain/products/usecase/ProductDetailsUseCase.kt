package com.souqApp.domain.products.usecase

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.products.remote.dto.AddProductToCartResponse
import com.souqApp.data.products.remote.dto.AddToFavoriteResponse
import com.souqApp.data.products.remote.dto.ProductDetailsEntity
import com.souqApp.data.products.remote.dto.ProductDetailsResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.entity.AddProductToCartEntity
import com.souqApp.domain.products.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductDetailsUseCase @Inject constructor(private val productsRepository: ProductsRepository) {

    suspend fun productDetails(productID: Int?): Flow<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsResponse>>> {
        return productsRepository.productDetails(productID)
    }

    suspend fun addOrRemoveProduct(productId: Int, combinationId: Int?): Flow<BaseResult<AddToFavoriteResponse, WrappedResponse<AddToFavoriteResponse>>>{
        return productsRepository.addOrRemoveProductToFavorite(productId, combinationId)
    }

    suspend fun addProductToCart(productId: Int, combinationId: Int?): Flow<BaseResult<AddProductToCartEntity, WrappedResponse<AddProductToCartResponse>>> {
        return productsRepository.addProductToCart(productId, combinationId)
    }
}