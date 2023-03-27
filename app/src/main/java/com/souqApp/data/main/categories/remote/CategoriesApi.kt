package com.souqApp.data.main.categories.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.common.CategoryEntity
import retrofit2.http.GET

interface CategoriesApi {
    @GET("v2/categories/getCategoriesTree")
    suspend fun categories(): WrappedListResponse<CategoryEntity>
}