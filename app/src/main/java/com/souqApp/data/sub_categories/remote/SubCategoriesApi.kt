package com.souqApp.data.sub_categories.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.sub_categories.remote.dto.SubCategoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SubCategoriesApi {
    @GET("v1/categories/getSubCategories")
    suspend fun subCategories(@Query("category_id") categoryId: Int): Response<WrappedListResponse<SubCategoryResponse>>
}