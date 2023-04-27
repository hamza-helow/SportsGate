package com.souqApp.presentation.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.souqApp.domain.products.GetProductsUseCase
import com.souqApp.domain.products.ProductsType
import com.souqApp.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val getProductsUseCase: GetProductsUseCase) :
    BaseViewModel() {

    val state: MutableLiveData<ProductsFragmentState> = MutableLiveData()

    fun loadProducts(id: Int, type: ProductsType) {
        when(type){
            ProductsType.PROMO ->   getProductsUseCase.request.promo = id
            ProductsType.TAG ->  getProductsUseCase.request.tag = id
            ProductsType.CATEGORY ->  getProductsUseCase.request.categoryId = id
        }

        viewModelScope.launch {
            val pager = Pager(
                config = PagingConfig(15, enablePlaceholders = false),
                pagingSourceFactory = { getProductsUseCase }
            )

            val pagedData = pager.flow.cachedIn(this).stateIn(this)

            state.value = ProductsFragmentState.OnProductsLoaded(pagedData.value)

        }
    }
}
