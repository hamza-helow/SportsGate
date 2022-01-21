package com.souqApp.data.sub_categories

import android.util.Log
import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.sub_categories.remote.SubCategoriesApi
import com.souqApp.data.sub_categories.remote.dto.SubCategoryResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.sub_categories.SubCategoriesRepository
import com.souqApp.domain.sub_categories.SubCategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SubCategoriesRepositoryImpl @Inject constructor(private val subCategoriesApi: SubCategoriesApi) :
    SubCategoriesRepository {
    override suspend fun subCategories(categoryId: Int): Flow<BaseResult<List<SubCategoryEntity>, WrappedListResponse<SubCategoryResponse>>> {
        return flow {

            val response = subCategoriesApi.subCategories(categoryId)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data.toEntity()))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }
}