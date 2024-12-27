package com.souqApp.domain.main.cart.usecase

import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResetCartUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun invoke(): Flow<BaseResult<String, String>> {
        return flow {
            val response = cartRepository.resetCart()

            if (response.status) {
                emit(BaseResult.Success(response.message))
            } else {
                emit(BaseResult.Errors(response.message))
            }

        }
    }

}