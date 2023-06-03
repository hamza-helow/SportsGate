package com.souqApp.presentation.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.GetProductsUseCase
import com.souqApp.domain.products.ProductsType
import com.souqApp.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val getProductsUseCase: GetProductsUseCase) :
    BaseViewModel() {

    val state: MutableLiveData<ProductsFragmentState> = MutableLiveData()
    var isLastPage = false

    private fun setLoading(isLoading: Boolean) {
        state.value = ProductsFragmentState.Loading(isLoading)
    }

    fun loadProducts(id: Int, type: ProductsType , pageNumber:Int = 1) {

        viewModelScope.launch {
            getProductsUseCase.execute(
                promo = if (type == ProductsType.PROMO) id else null,
                tag = if (type == ProductsType.TAG) id else null,
                type = if (type == ProductsType.CATEGORY) id else null,
                page = pageNumber
            ).onStart {
                if (pageNumber == 1)
                    setLoading(true)
            }.catch {
                setLoading(false)
            }.collect {
                setLoading(false)
                when (it) {
                    is BaseResult.Errors -> Unit
                    is BaseResult.Success -> {
                        isLastPage = it.data.products.isEmpty()
                        state.value = ProductsFragmentState.OnProductsLoaded(it.data.products)
                    }
                }
            }
        }
    }
}
