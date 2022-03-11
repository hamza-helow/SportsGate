package com.souqApp.data.search

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.search.remote.SearchApi
import com.souqApp.data.search.remote.SearchEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val searchApi: SearchApi) :
    SearchRepository {
    override suspend fun getSearchProducts(
        product_name: String,
        sort_type: Int,
        page: Int
    ): Flow<BaseResult<SearchEntity, WrappedResponse<SearchEntity>>> {

        return flow {

            val response = searchApi.getSearchProducts(product_name, sort_type, page)
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