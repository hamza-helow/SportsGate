package com.souqApp.data.products.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {

    @GET("v2/products/getProducts")
    suspend fun getProducts(
        @Query("categoryId") type: Int?,
        @Query("page") page: Int?,
        @Query("search") search: String?,
        @Query("tag") tag: Int?,
        @Query("promo") promo: Int?,
        @Query("recommended") recommended: Int?
    ): Response<WrappedListResponse<ProductEntity>>


}