package com.souqApp.data.main.categories

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.categories.remote.CategoriesApi
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.domain.main.categories.CategoriesRepository
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(private val categoriesApi: CategoriesApi) :
    CategoriesRepository {
    override suspend fun categories(): WrappedListResponse<CategoryEntity> {
        return categoriesApi.categories()
    }

}