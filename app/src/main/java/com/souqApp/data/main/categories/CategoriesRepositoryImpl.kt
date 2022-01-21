package com.souqApp.data.main.categories

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.categories.remote.CategoriesApi
import com.souqApp.data.main.common.CategoryResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.categories.CategoriesRepository
import com.souqApp.domain.main.home.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(private val categoriesApi: CategoriesApi) :
    CategoriesRepository {
    override suspend fun categories(): Flow<BaseResult<List<CategoryEntity>, WrappedListResponse<CategoryResponse>>> {
        return flow {
            val response = categoriesApi.categories()
            val isSuccessful = response.status

            if (isSuccessful) {
                val body = response.data!!
                emit(BaseResult.Success(body.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}