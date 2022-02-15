package com.souqApp.data.main.home.remote

import android.util.Log
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.home.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi) : HomeRepository {
    override suspend fun home(): Flow<BaseResult<HomeEntity, WrappedResponse<HomeEntity>>> {
        return flow {
            val response = homeApi.getHome()
            val isSuccessful = response.body()?.status

            Log.e("ererR", "${response.code()}")

            if (isSuccessful == true) {
                val body = response.body()!!.data!!
                val homeEntity = HomeEntity(
                    body.cart_products_count,
                    body.show_location,
                    body.products_ads,
                    body.categories,
                    body.new_products,
                    body.best_selling_products,
                    body.recommended_products
                )
                emit(BaseResult.Success(homeEntity))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }
        }
    }
}