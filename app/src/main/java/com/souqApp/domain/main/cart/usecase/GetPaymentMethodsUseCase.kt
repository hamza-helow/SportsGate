package com.souqApp.domain.main.cart.usecase

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.cart.remote.dto.PaymentMethodResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.CartRepository
import com.souqApp.domain.main.cart.entity.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPaymentMethodsUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun invoke(): Flow<BaseResult<List<PaymentMethodEntity>, WrappedListResponse<PaymentMethodResponse>>> {
        return flow {
            val response = cartRepository.getPaymentMethods()
            if (response.status) {
                emit(BaseResult.Success(data = response.data.orEmpty().map { it.toEntity() }))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}