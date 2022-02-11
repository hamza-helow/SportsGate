package com.souqApp.data.main.cart.remote

import android.util.Log
import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.*
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.CartRepository
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import com.souqApp.domain.main.cart.entity.UpdateProductQtyEntity
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


    override suspend fun checkout(checkoutRequest: CheckoutRequest): Flow<BaseResult<CheckoutEntity, WrappedResponse<CheckoutResponse>>> {
        TODO("Not yet implemented")
    }
    override suspend fun checkCouponCode(couponCode: String): Flow<BaseResult<Nothing, WrappedResponse<Nothing>>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProductFromCart(productId: Int): Flow<Boolean> {
        return flow {
            val response = cartApi.deleteProductFromCart(productId)
            val isSuccessful = response.body()?.status

            Log.e("ERer" , "qwewqe $isSuccessful")

            if (isSuccessful == true) {
                emit(true)
            } else
                emit(false)
        }
    }

    override suspend fun updateProductQty(
        productId: Int,
        qty: Int
    ): Flow<BaseResult<UpdateProductQtyEntity, WrappedResponse<UpdateProductQtyResponse>>> {

        return flow {
            val response = cartApi.updateProductQty(productId, qty)
            val isSuccessful = response.body()?.status
            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data = data.toEntity()))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }



}