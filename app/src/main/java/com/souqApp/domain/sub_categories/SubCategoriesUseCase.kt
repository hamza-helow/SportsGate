package com.souqApp.domain.sub_categories

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.sub_categories.remote.dto.SubCategoryResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubCategoriesUseCase @Inject constructor(private val subCategoriesRepository: SubCategoriesRepository) {

    suspend fun subCategories(categoryId: Int): Flow<BaseResult<List<SubCategoryEntity>, WrappedListResponse<SubCategoryResponse>>> {
        return subCategoriesRepository.subCategories(categoryId)
    }
}