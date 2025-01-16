package com.souqApp.presentation.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.products.entity.ProductsType
import com.souqApp.domain.products.usecase.GetProductsUseCase
import com.souqApp.infra.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    val id: Int? by lazy { savedStateHandle.get<Int>(Constant.CATEGORY_ID) }
    val type: ProductsType? by lazy { savedStateHandle.get<ProductsType>(Constant.TYPE) }

    val productsLiveData: LiveData<PagingData<ProductEntity>> by lazy {
        val request = getProductsUseCase.request
        request.promo = id.takeIf { type == ProductsType.PROMO }
        request.tag = id.takeIf { type == ProductsType.TAG }
        request.categoryId = id.takeIf { type == ProductsType.CATEGORY }
        getProductsUseCase.invoke().cachedIn(viewModelScope)
    }

}
