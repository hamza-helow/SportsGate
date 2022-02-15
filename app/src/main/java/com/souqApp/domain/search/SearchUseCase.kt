package com.souqApp.domain.search

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.search.remote.SearchEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val searchRepository: SearchRepository) {


    suspend fun getSearchProducts(
        product_name: String,
        sort_type: Int,
        page: Int
    ): Flow<BaseResult<SearchEntity, WrappedResponse<SearchEntity>>> {
        return searchRepository.getSearchProducts(product_name, sort_type, page)
    }
}