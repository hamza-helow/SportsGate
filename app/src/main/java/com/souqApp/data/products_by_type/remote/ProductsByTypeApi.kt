package com.souqApp.data.products_by_type.remote

import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeRequest
import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeResponse
import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductsByTypeApi {

    @GET("v1/products/getProductsByType")
    suspend fun getProductsByType(
        @Query("type") type: Int,
        @Query("sub_category_id") subCategoryId: Int,
        @Query("page") page: Int
    ): Response<WrappedResponse<ProductsByTypeResponse>>
}