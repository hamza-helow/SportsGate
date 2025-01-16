package com.souqApp.domain.main.cart.usecase

import com.souqApp.domain.main.cart.CartRepository
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.infra.utils.getTimestampInSeconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckCouponUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val sharedPrefs: SharedPrefs
) {

    fun execute(couponCode: String): Flow<Boolean> {
        return flow {
            val response = cartRepository.checkCouponCode(couponCode)
            sharedPrefs.setLastCheckOutDetailsTimeStamp(getTimestampInSeconds())
            emit(response.status)
        }
    }

}