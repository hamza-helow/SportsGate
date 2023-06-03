package com.souqApp.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.products.GetProductsUseCaseP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getProductsUseCaseP: GetProductsUseCaseP,

    ) : ViewModel() {

    private val _state = MutableLiveData<SearchActivityState>()
    val state: LiveData<SearchActivityState> get() = _state

    fun search(search: String) {

        getProductsUseCaseP.request.search = search

        viewModelScope.launch {
            val pagedData = Pager(
                config = PagingConfig(15, enablePlaceholders = false),
                pagingSourceFactory = { getProductsUseCaseP }
            ).flow.cachedIn(this).stateIn(this)

            _state.value = SearchActivityState.Loaded(pagedData.value)
        }
    }


}

sealed class SearchActivityState {

    data class Loading(val isLoading: Boolean) : SearchActivityState()
    data class Error(val throwable: Throwable) : SearchActivityState()
    data class Loaded(val searchEntity: PagingData<ProductEntity>) : SearchActivityState()
    data class ErrorLoad(val response: WrappedListResponse<ProductEntity>) : SearchActivityState()
}