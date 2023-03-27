package com.souqApp.presentation.main.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.categories.CategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val categoriesUseCase: CategoriesUseCase) :
    ViewModel() {

    private val state = MutableStateFlow<CategoriesFragmentState>(CategoriesFragmentState.Init)
    val mState: StateFlow<CategoriesFragmentState> get() = state


    fun setLoading() {
        state.value = CategoriesFragmentState.IsLoading(true)
    }

    private fun hideLoading() {
        state.value = CategoriesFragmentState.IsLoading(false)
    }

    fun showToast(message: String) {
        state.value = CategoriesFragmentState.ShowToast(message)
    }

    private fun categoriesLoaded(categories: List<CategoryEntity>) {
        state.value = CategoriesFragmentState.CategoriesLoaded(categories)
    }

    private fun categoriesLoadError(response: WrappedListResponse<CategoryEntity>) {
        state.value = CategoriesFragmentState.CategoriesError(response)
    }

    @Inject
    fun categories() {
        viewModelScope.launch {
            categoriesUseCase.categories()
                .onStart { setLoading() }
                .catch {
                    hideLoading()
                    showToast(it.stackTraceToString())
                }
                .collect {
                    hideLoading()
                    when (it) {
                        is BaseResult.Success -> categoriesLoaded(it.data)
                        is BaseResult.Errors -> categoriesLoadError(it.error)
                    }
                }
        }
    }

}

sealed class CategoriesFragmentState {

    object Init : CategoriesFragmentState()

    data class IsLoading(val isLoading: Boolean) : CategoriesFragmentState()
    data class ShowToast(val message: String) : CategoriesFragmentState()
    data class CategoriesLoaded(val categories: List<CategoryEntity>) : CategoriesFragmentState()
    data class CategoriesError(val response: WrappedListResponse<CategoryEntity>) :
        CategoriesFragmentState()
}