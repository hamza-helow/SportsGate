package com.souqApp.data.main.home.remote

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.CheckUpdateResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.domain.main.home.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi) : HomeRepository {
    override suspend fun getHome(): WrappedResponse<HomeResponse> {
        return homeApi.getHome()
    }


    override suspend fun checkUpdate(): WrappedResponse<CheckUpdateResponse> {
        return homeApi.checkUpdate()
    }
}