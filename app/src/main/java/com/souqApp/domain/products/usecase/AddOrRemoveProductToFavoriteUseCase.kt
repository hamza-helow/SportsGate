package com.souqApp.domain.products.usecase

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.products.remote.dto.AddToFavoriteResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.ProductsRepository
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.infra.utils.getTimestampInSeconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddOrRemoveProductToFavoriteUseCase @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val sharedPrefs: SharedPrefs
) {

    suspend fun invoke(
        productId: Int?,
        combinationId: Int? = null
    ): Flow<BaseResult<AddToFavoriteResponse, WrappedResponse<AddToFavoriteResponse>>> {
        return flow {
            val response = productsRepository.addOrRemoveProductToFavorite(
                productId,
                combinationId
            )

            if (response.status) {
                sharedPrefs.setLastWishListUpdateTimeStamp(getTimestampInSeconds())
                emit(BaseResult.Success(response.data))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}