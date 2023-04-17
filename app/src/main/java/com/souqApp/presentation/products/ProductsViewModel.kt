package com.souqApp.presentation.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.souqApp.domain.products.GetProductsUseCase
import com.souqApp.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val getProductsUseCase: GetProductsUseCase) :
    BaseViewModel() {

    val state: MutableLiveData<ProductsFragmentState> = MutableLiveData()

    fun loadProducts(id: Int, isPromo: Boolean) {
        viewModelScope.launch {
            if (isPromo)
                getProductsUseCase.request.promo = id
            else
                getProductsUseCase.request.categoryId = id
            viewModelScope.launch {
                val pagedData = Pager(
                    config = PagingConfig(15, enablePlaceholders = false),
                    pagingSourceFactory = { getProductsUseCase }
                ).flow.cachedIn(this).stateIn(this)

                state.value = ProductsFragmentState.OnProductsLoaded(pagedData.value)
            }
        }
    }
}
