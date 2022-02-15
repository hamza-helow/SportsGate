package com.souqApp.domain.main.categories

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(private val categoriesRepository: CategoriesRepository) {

    suspend fun categories(): Flow<BaseResult<List<CategoryEntity>, WrappedListResponse<CategoryEntity>>> {
        return categoriesRepository.categories()
    }
}