package com.souqApp.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.search.remote.SearchEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.search.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchUseCase: SearchUseCase) : ViewModel() {

    private val _state = MutableLiveData<SearchActivityState>()
    val state: LiveData<SearchActivityState> get() = _state


    private fun setLoading(isLoading: Boolean) {
        _state.value = SearchActivityState.Loading(isLoading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = SearchActivityState.Error(throwable)
    }

    private fun onLoaded(searchEntity: SearchEntity) {
        _state.value = SearchActivityState.Loaded(searchEntity)
    }

    private fun onErrorLoad(response: WrappedResponse<SearchEntity>) {
        _state.value = SearchActivityState.ErrorLoad(response)
    }

    fun search(productName: String, page: Int) {

        viewModelScope.launch {
            searchUseCase.getSearchProducts(productName, 1, page)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onLoaded(it.data)
                        is BaseResult.Errors -> onErrorLoad(it.error)
                    }
                }
        }
    }


}

sealed class SearchActivityState {

    data class Loading(val isLoading: Boolean) : SearchActivityState()
    data class Error(val throwable: Throwable) : SearchActivityState()
    data class Loaded(val searchEntity: SearchEntity) : SearchActivityState()
    data class ErrorLoad(val response: WrappedResponse<SearchEntity>) : SearchActivityState()
}