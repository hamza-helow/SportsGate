package com.souqApp.domain.main.cart

import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResetCartUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun invoke(): Flow<BaseResult<String, String>>  {
        return cartRepository.resetCart()
    }

}