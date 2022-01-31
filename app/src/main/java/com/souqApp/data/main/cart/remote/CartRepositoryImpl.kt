package com.souqApp.data.main.cart.remote

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.CartRepository
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cartApi: CartApi) : CartRepository {
    override suspend fun getCartDetails(): Flow<BaseResult<CartDetailsEntity, WrappedResponse<CartDetailsResponse>>> {

        return flow {

            val response = cartApi.getCartDetails()
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {

                val data = response.body()!!.data!!
                emit(BaseResult.Success(data.toEntity()))

            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }

    override suspend fun getCheckoutDetails(): Flow<BaseResult<CheckoutDetailsEntity, WrappedResponse<CheckoutDetailsResponse>>> {
        TODO("Not yet implemented")
    }

    override suspend fun checkout(checkoutRequest: CheckoutRequest): Flow<BaseResult<CheckoutEntity, WrappedResponse<CheckoutResponse>>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProductFromCart(productId: Int): Flow<BaseResult<Nothing, WrappedResponse<Nothing>>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProductQty(
        productId: Int,
        qty: Int
    ): Flow<BaseResult<Nothing, WrappedResponse<Nothing>>> {
        TODO("Not yet implemented")
    }

    override suspend fun checkCouponCode(couponCode: String): Flow<BaseResult<Nothing, WrappedResponse<Nothing>>> {
        TODO("Not yet implemented")
    }
}