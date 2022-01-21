package com.souqApp.domain.main.categories

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.common.CategoryResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.home.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    suspend fun categories(): Flow<BaseResult<List<CategoryEntity>, WrappedListResponse<CategoryResponse>>>
}