package com.souqApp.presentation.sub_categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.sub_categories.remote.dto.SubCategoryResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.sub_categories.SubCategoriesUseCase
import com.souqApp.domain.sub_categories.SubCategoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoriesViewModel @Inject constructor(private val subCategoriesUseCase: SubCategoriesUseCase) :
    ViewModel() {
    private val state = MutableLiveData<SubCategoriesActivityState>(SubCategoriesActivityState.Init)
    val mState: LiveData<SubCategoriesActivityState> get() = state

    private fun whenLoading(isLoading: Boolean) {
        state.value = SubCategoriesActivityState.Loading(isLoading)
    }

    private fun whenLoaded(subCategories: List<SubCategoryEntity>) {
        state.value = SubCategoriesActivityState.Loaded(subCategories)
    }

    private fun whenErrorLoading(response: WrappedListResponse<SubCategoryResponse>) {
        state.value = SubCategoriesActivityState.ErrorLoading(response)
    }

    private fun catchError(message: String) {
        state.value = SubCategoriesActivityState.CatchError(message)
    }


    fun getSubCategories(categoryId: Int) {
        viewModelScope.launch {
            subCategoriesUseCase
                .subCategories(categoryId)
                .onStart { whenLoading(true) }
                .catch {
                    whenLoading(false)
                    catchError(it.stackTraceToString())
                }.collect {
                    whenLoading(false)
                    when (it) {
                        is BaseResult.Success -> whenLoaded(it.data)
                        is BaseResult.Errors -> whenErrorLoading(it.error)
                    }
                }
        }
    }

}

sealed class SubCategoriesActivityState {
    object Init : SubCategoriesActivityState()

    data class Loading(val isLoading: Boolean) : SubCategoriesActivityState()

    data class CatchError(val message: String) : SubCategoriesActivityState()

    data class Loaded(val subCategories: List<SubCategoryEntity>) : SubCategoriesActivityState()

    data class ErrorLoading(val response: WrappedListResponse<SubCategoryResponse>) :
        SubCategoriesActivityState()
}