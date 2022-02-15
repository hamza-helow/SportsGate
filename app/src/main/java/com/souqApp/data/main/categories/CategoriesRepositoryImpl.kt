package com.souqApp.data.main.categories

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.categories.remote.CategoriesApi
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.categories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(private val categoriesApi: CategoriesApi) :
    CategoriesRepository {
    override suspend fun categories(): Flow<BaseResult<List<CategoryEntity>, WrappedListResponse<CategoryEntity>>> {
        return flow {
            val response = categoriesApi.categories()
            val isSuccessful = response.status

            if (isSuccessful) {
                val body = response.data!!
                emit(BaseResult.Success(body))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}