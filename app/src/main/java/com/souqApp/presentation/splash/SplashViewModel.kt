package com.souqApp.presentation.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.usecase.GetPagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val getPagesUseCase: GetPagesUseCase) :
    ViewModel() {

    val pagesLiveData: MutableLiveData<BaseResult<List<PageEntity>, WrappedListResponse<PageEntity>>> =
        MutableLiveData()

    @Inject
    fun getPages() {
        viewModelScope.launch {
            getPagesUseCase.invoke()
                .catch {  }
                .collect {
                    pagesLiveData.value = it
                }
        }
    }


}
