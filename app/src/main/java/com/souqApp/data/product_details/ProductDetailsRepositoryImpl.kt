package com.souqApp.data.product_details

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsApi
import com.souqApp.data.product_details.remote.ProductDetailsResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.product_details.ProductDetailsEntity
import com.souqApp.domain.product_details.ProductDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductDetailsRepositoryImpl @Inject constructor(private val productsDetailsApi: ProductDetailsApi) :
    ProductDetailsRepository {
    override suspend fun productDetails(productID: Int): Flow<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsResponse>>> {
        return flow {
            val response = productsDetailsApi.productDetails(productID)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data.toEntity()))
            } else {
                emit(BaseResult.Errors(response.body()!!))
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

    override suspend fun addProductToCart(productId: Int): Flow<Boolean> {
        return flow {
            val response = productsDetailsApi.addProductToCart(productId)
            val isSuccessful = response.body()?.status
            if (isSuccessful == true) {
                emit(true)
            } else {
                emit(false)
            }
        }
    }
}