package com.souqApp.presentation.main.more.page_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.PageDetailsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.usecase.GetPageDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageDetailsViewModel @Inject constructor(private val getPageDetailsUseCase: GetPageDetailsUseCase) :
    ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val pageDetailsLiveData: MutableLiveData<BaseResult<PageDetailsEntity, WrappedResponse<PageDetailsEntity>>> =
        MutableLiveData()

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }

    fun getPageDetails(pageId: Int?) {
        viewModelScope.launch {
            getPageDetailsUseCase.invoke(pageId)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    pageDetailsLiveData.value = it
                }
        }
    }

}


