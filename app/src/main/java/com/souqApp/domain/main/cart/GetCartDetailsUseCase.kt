package com.souqApp.domain.main.cart

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartDetailsUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun execute(): Flow<BaseResult<CartDetailsEntity, WrappedResponse<CartDetailsResponse>>> {
        return cartRepository.getCartDetails()
    }
}