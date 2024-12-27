package com.souqApp.domain.products.usecase

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.ProductsRepository
import com.souqApp.infra.utils.SharedPrefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavoriteProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val sharedPrefs: SharedPrefs
) {

    suspend fun invoke(): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductEntity>>> {
        return flow {
            val updated = sharedPrefs.getLastWishListUpdateTimeStamp()
            val response = productsRepository.getFavoriteProducts(updated)
            if (response.status) {
                emit(BaseResult.Success(response.data.orEmpty()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}