package com.souqApp.domain.main.categories

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(private val categoriesRepository: CategoriesRepository) {

    suspend fun invoke(): Flow<BaseResult<List<CategoryEntity>, WrappedListResponse<CategoryEntity>>> {
        return flow {
            val response = categoriesRepository.categories()
            val isSuccessful = response.status

            if (isSuccessful) {
                val body = response.data.orEmpty()
                emit(BaseResult.Success(body))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}