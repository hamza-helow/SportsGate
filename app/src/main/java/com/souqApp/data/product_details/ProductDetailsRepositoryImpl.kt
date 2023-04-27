package com.souqApp.data.product_details

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsApi
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.data.product_details.remote.ProductDetailsResponse
import com.souqApp.data.product_details.remote.VariationProductPriceInfoResponse
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

    override suspend fun addOrRemoveProduct(productId: Int): Flow<Boolean> {
        return flow {

            val response = productsDetailsApi.addOrRemoveProduct(productId)

            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                emit(true)
            } else {
                emit(false)
            }
        }
    }

    override suspend fun addProductToCart(productId: Int, combinationId: Int?): Flow<Boolean> {
        return flow {
            val response = productsDetailsApi.addProductToCart(productId, combinationId)
            val isSuccessful = response.body()?.status
            if (isSuccessful == true) {
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
            val isSuccessful = response.body()?.status == true

            if (isSuccessful) {
                response.body()?.data?.let {
                    emit(BaseResult.Success(it.toEntity()))
                }

            } else {
                if (response.body() != null) {
                    emit(BaseResult.Errors(response.body()!!))
                }
            }
        }
    }
}