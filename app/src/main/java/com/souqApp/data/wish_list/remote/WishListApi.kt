package com.souqApp.data.wish_list.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import retrofit2.Response
import retrofit2.http.GET

interface WishListApi {

    @GET("v1/users/favourites/getAll")
    suspend fun getAll(): Response<WrappedListResponse<ProductEntity>>
}