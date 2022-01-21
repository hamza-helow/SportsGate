package com.souqApp.domain.main.categories

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.CategoryResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.home.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(private val categoriesRepository: CategoriesRepository) {

    suspend fun categories(): Flow<BaseResult<List<CategoryEntity>, WrappedListResponse<CategoryResponse>>> {
        return categoriesRepository.categories()
    }
}