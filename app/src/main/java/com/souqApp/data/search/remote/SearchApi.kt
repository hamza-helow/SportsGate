package com.souqApp.data.search.remote

import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("v1/products/getSearchProducts")
    suspend fun getSearchProducts(
        @Query("product_name") product_name: String,
        @Query("sort_type") sort_type: Int,
        @Query("page") page: Int
    ): Response<WrappedResponse<SearchEntity>>


}