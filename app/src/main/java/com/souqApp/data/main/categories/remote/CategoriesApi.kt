package com.souqApp.data.main.categories.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.CategoryResponse
import retrofit2.http.GET

interface CategoriesApi {

    @GET("v1/categories/getAll")
    suspend fun categories(): WrappedListResponse<CategoryResponse>
}