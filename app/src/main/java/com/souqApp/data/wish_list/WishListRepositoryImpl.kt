package com.souqApp.data.wish_list

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.data.wish_list.remote.WishListApi
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.wish_list.WishListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WishListRepositoryImpl @Inject constructor(private val wishListApi: WishListApi) :
    WishListRepository {
    override suspend fun getAll(): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductEntity>>> {
        return flow {
            val response = wishListApi.getAll()
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }
}