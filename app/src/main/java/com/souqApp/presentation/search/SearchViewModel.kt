package com.souqApp.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.products.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val getProductsUseCase: GetProductsUseCase) :
    ViewModel() {

    var searchResultLiveData: LiveData<PagingData<ProductEntity>> = MutableLiveData()

    fun search(search: String) {
        getProductsUseCase.request.search = search
        searchResultLiveData = getProductsUseCase.invoke()
    }
}