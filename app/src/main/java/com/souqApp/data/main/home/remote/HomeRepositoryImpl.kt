package com.souqApp.data.main.home.remote

import android.util.Log
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.home.HomeRepository
import com.souqApp.domain.main.home.entity.HomeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi) : HomeRepository {
    override suspend fun home(): Flow<BaseResult<HomeEntity, WrappedResponse<HomeResponse>>> {
        return flow {
            val response = homeApi.getHome()
            val isSuccessful = response.body()?.status

            Log.e("ererR", "${response.code()}")

            if (isSuccessful == true) {
                val body = response.body()!!.data!!
                val homeEntity = HomeEntity(
                    body.cartProductsCount,
                    body.showLocation,
                    body.ads,
                    body.categories,
                    body.newProducts,
                    body.bestSellingProducts,
                    body.recommendedProducts
                )
                emit(BaseResult.Success(homeEntity))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }
        }
    }
}