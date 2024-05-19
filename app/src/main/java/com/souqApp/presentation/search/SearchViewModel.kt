package com.souqApp.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.products.GetProductsUseCaseP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val getProductsUseCase: GetProductsUseCaseP) :
    ViewModel() {

    val searchResultLiveData: MutableLiveData<PagingData<ProductEntity>> = MutableLiveData()

    fun search(search: String) {

        getProductsUseCase.request.search = search

        viewModelScope.launch {
            val pagedData = Pager(
                config = PagingConfig(15, enablePlaceholders = false),
                pagingSourceFactory = { getProductsUseCase }
            ).flow.cachedIn(this).stateIn(this)

            searchResultLiveData.value = pagedData.value
        }
    }


}