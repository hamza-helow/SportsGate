package com.souqApp.data.wish_list.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import retrofit2.http.GET

interface WishListApi {

    @GET("v2/users/favorites/getFavoriteProducts")
    suspend fun getAll(): WrappedListResponse<ProductEntity>
}