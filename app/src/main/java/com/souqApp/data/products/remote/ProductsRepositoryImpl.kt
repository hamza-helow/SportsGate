package com.souqApp.data.products.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val productsApi: ProductsApi) :
    ProductsRepository {

    override suspend fun getProducts(categoryId: Int): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductEntity>>> {
        return flow {
            val response = productsApi.getProducts(categoryId)
            val isSuccessful = response.body()?.status == true

            if (isSuccessful) {
                emit(BaseResult.Success(response.body()?.data.orEmpty()))
            } else
                response.body()?.let { emit(BaseResult.Errors(it)) }
        }
    }

}