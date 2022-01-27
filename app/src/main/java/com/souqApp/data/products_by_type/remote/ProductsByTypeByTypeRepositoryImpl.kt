package com.souqApp.data.products_by_type.remote

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeRequest
import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.products_by_type.ProductsByTypeRepository
import com.souqApp.domain.products_by_type.ProductsByTypeEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsByTypeByTypeRepositoryImpl @Inject constructor(val productsByTypeApi: ProductsByTypeApi) :
    ProductsByTypeRepository {
    override suspend fun getProductsByType(request: ProductsByTypeRequest): Flow<BaseResult<ProductsByTypeEntity, WrappedResponse<ProductsByTypeResponse>>> {
        return flow {

            val response = productsByTypeApi.getProductsByType(
                request.type,
                request.sub_category_id,
                request.page
            )
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {

                val data = response.body()!!.data!!
                emit(BaseResult.Success(data.toEntity()))

            } else {
                emit(BaseResult.Errors(response.body()!!))
            }
        }
    }
}