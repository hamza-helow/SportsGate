package com.souqApp.domain.wish_list

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishListUseCase @Inject constructor(private val wishListRepository: WishListRepository) {

    suspend fun getAll(): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductEntity>>> {
        return wishListRepository.getAll()
    }
}