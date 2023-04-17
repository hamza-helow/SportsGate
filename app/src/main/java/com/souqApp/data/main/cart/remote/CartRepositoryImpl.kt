package com.souqApp.data.main.cart.remote

import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse
import com.souqApp.domain.main.cart.entity.CheckoutDetailsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.CartRepository
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import com.souqApp.domain.main.cart.entity.UpdateProductCartEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cartApi: CartApi) : CartRepository {
    override suspend fun getCartDetails(): Flow<BaseResult<CartDetailsEntity, WrappedResponse<CartDetailsResponse>>> {
        return flow {
            val response = cartApi.getCartDetails()
            val isSuccessful = response.body()?.status
            if (isSuccessful == true) {
                response.body()?.data?.let {
                    emit(BaseResult.Success(it.toEntity()))
                }
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }


    override suspend fun deleteProductFromCart(cartItemId: Int): Flow<BaseResult<UpdateProductCartEntity, WrappedResponse<UpdateProductCartResponse>>> {
        return flow {
            val response = cartApi.deleteProductFromCart(cartItemId)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data = data.toEntity()))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }
        }
    }

    override suspend fun updateProductQty(
        productId: Int,
        qty: Int,
        combinationId: Int?
    ): Flow<BaseResult<UpdateProductCartEntity, WrappedResponse<UpdateProductCartResponse>>> {

        return flow {
            val response = cartApi.updateProductQty(productId, qty, combinationId)
            val isSuccessful = response.body()?.status
            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data = data.toEntity()))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }


    override suspend fun getCheckoutDetails(deliveryOptionId: Int?): Flow<BaseResult<CheckoutDetailsEntity, WrappedResponse<CheckoutDetailsResponse>>> {
        return flow {
            val response = cartApi.getCheckoutDetails(deliveryOptionId)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data = data.toEntity()))

            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }


    override suspend fun checkout(
        couponCode: String?,
        addressId: Int?,
        deliveryOptionId: Int?
    ): Flow<BaseResult<CheckoutEntity, WrappedResponse<CheckoutResponse>>> {
        return flow {

            val response = cartApi.checkout(couponCode, addressId, deliveryOptionId)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data = data.toEntity()))

            } else {
                emit(BaseResult.Errors(response.body()!!))
            }
        }
    }

    override suspend fun checkCouponCode(couponCode: String): Flow<Boolean> {
        return flow {
            val response = cartApi.checkCouponCode(couponCode)
            val isSuccessful = response.body()?.status
            emit(isSuccessful == true)
        }
    }

}