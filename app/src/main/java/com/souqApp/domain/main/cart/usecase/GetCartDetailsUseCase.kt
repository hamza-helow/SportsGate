package com.souqApp.domain.main.cart.usecase

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.CartRepository
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.infra.utils.SharedPrefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCartDetailsUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val sharedPrefs: SharedPrefs
) {
    suspend fun execute(): Flow<BaseResult<CartDetailsEntity, WrappedResponse<CartDetailsResponse>>> {
        return flow {
            val updated = sharedPrefs.getLastCartUpdateTimeStamp()
            val response = cartRepository.getCartDetails(updated)
            if (response.status) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}