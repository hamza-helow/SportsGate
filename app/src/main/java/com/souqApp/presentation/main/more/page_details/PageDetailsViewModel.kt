package com.souqApp.presentation.main.more.page_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.PageDetailsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.GetPageDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageDetailsViewModel @Inject constructor(private val getPageDetailsUseCase: GetPageDetailsUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<PageDetailsState>()
    val state: LiveData<PageDetailsState> get() = _state


    private fun setLoading(isLoading: Boolean) {
        _state.value = PageDetailsState.Loading(isLoading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = PageDetailsState.Error(throwable)
    }

    private fun onLoaded(pageDetailsEntity: PageDetailsEntity) {
        _state.value = PageDetailsState.Loaded(pageDetailsEntity)
    }


    private fun onErrorLoad(response: WrappedResponse<PageDetailsEntity>) {
        _state.value = PageDetailsState.ErrorLoad(response)
    }

    fun getPageDetails(pageId: Int) {
        viewModelScope.launch {
            getPageDetailsUseCase.invoke(pageId)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Errors -> onErrorLoad(it.error)
                        is BaseResult.Success -> onLoaded(it.data)
                    }
                }
        }
    }

}


sealed class PageDetailsState {

    data class Loading(val isLoading: Boolean) : PageDetailsState()
    data class Error(val throwable: Throwable) : PageDetailsState()
    data class Loaded(val details: PageDetailsEntity) : PageDetailsState()

    data class ErrorLoad(val response: WrappedResponse<PageDetailsEntity>) : PageDetailsState()
}