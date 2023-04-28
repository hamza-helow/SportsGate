package com.souqApp.data.product_details

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.*
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.product_details.ProductDetailsRepository
import com.souqApp.domain.product_details.VariationProductPriceInfoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductDetailsRepositoryImpl @Inject constructor(private val productsDetailsApi: ProductDetailsApi) :
    ProductDetailsRepository {

    override suspend fun productDetails(productID: Int): Flow<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsResponse>>> {
        return flow {
            val response = productsDetailsApi.productDetails(productID)
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
            val response = productsDetailsApi.addOrRemoveProductToFavorite(productId, combinationId)

            if (response.status) {
                emit(BaseResult.Success(response.data))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun addProductToCart(productId: Int, combinationId: Int?): Flow<Boolean> {
        return flow {
            val response = productsDetailsApi.addProductToCart(productId, combinationId)
            if (response.status) {
                emit(true)
            } else {
                emit(false)
            }
        }
    }

    override suspend fun getVariationProductPriceInfo(
        productId: Int,
        label: String
    ): Flow<BaseResult<VariationProductPriceInfoEntity, WrappedResponse<VariationProductPriceInfoResponse>>> {
        return flow {
            val response = productsDetailsApi.getVariationProductPriceInfo(productId, label)
            if (response.status) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}