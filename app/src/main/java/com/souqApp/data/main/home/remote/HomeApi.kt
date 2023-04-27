package com.souqApp.data.main.home.remote

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import retrofit2.http.GET

interface HomeApi {

    @GET("v2/home")
    suspend fun getHome(): WrappedResponse<HomeResponse>
}