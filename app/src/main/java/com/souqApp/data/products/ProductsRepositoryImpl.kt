package com.souqApp.data.products

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.data.products.remote.*
import com.souqApp.data.products.remote.dto.AddProductToCartResponse
import com.souqApp.data.products.remote.dto.AddToFavoriteResponse
import com.souqApp.data.products.remote.dto.ProductDetailsEntity
import com.souqApp.data.products.remote.dto.ProductDetailsResponse
import com.souqApp.data.products.remote.dto.VariationProductPriceInfoResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.entity.AddProductToCartEntity
import com.souqApp.domain.products.ProductsRepository
import com.souqApp.domain.products.entity.VariationProductPriceInfoEntity
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
    ): WrappedListResponse<ProductEntity> {
        return productsApi.getProducts(type, page, search, tag, promo, recommended)
    }


    override suspend fun productDetails(productID: Int?): Flow<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsResponse>>> {
        return flow {
            val response = productsApi.productDetails(productID)
            val isSuccessful = response.status
            if (isSuccessful) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun addOrRemoveProductToFavorite(
        productId: Int,
        combinationId: Int?
    ): Flow<BaseResult<AddToFavoriteResponse, WrappedResponse<AddToFavoriteResponse>>> {
        return flow {
            val response = productsApi.addOrRemoveProductToFavorite(
                productId,
                combinationId
            )

            if (response.status) {
                emit(BaseResult.Success(response.data))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun addProductToCart(
        productId: Int,
        combinationId: Int?
    ): Flow<BaseResult<AddProductToCartEntity, WrappedResponse<AddProductToCartResponse>>> {
        return flow {
            val response = productsApi.addProductToCart(productId, combinationId)
            if (response.status) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun getVariationProductPriceInfo(
        productId: Int,
        label: String
    ): Flow<BaseResult<VariationProductPriceInfoEntity, WrappedResponse<VariationProductPriceInfoResponse>>> {
        return flow {
            val response = productsApi.getVariationProductPriceInfo(productId, label)
            if (response.status) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun getFavoriteProducts(): WrappedListResponse<ProductEntity> {
        return productsApi.getFavoriteProducts()
    }
}