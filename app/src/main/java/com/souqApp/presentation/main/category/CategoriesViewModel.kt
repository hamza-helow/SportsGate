package com.souqApp.presentation.main.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.categories.CategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val categoriesUseCase: CategoriesUseCase) :
    ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val categoriesLiveData: MutableLiveData<BaseResult<List<CategoryEntity>, WrappedListResponse<CategoryEntity>>> =
        MutableLiveData()

    fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }

    @Inject
    fun getCategories() {
        viewModelScope.launch {
            categoriesUseCase.invoke()
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    categoriesLiveData.value = it
                }
        }
    }

}
