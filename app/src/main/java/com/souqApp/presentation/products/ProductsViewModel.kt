package com.souqApp.presentation.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.GetProductsUseCase
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

    fun loadProducts(categoryId: Int) {
        viewModelScope.launch {

            getProductsUseCase.execute(categoryId).onStart {
                state.value = ProductsFragmentState.Loading(true)
            }.catch {
                state.value = ProductsFragmentState.Loading(false)
            }.collect {
                state.value = ProductsFragmentState.Loading(false)
                when (it) {
                    is BaseResult.Errors -> {

                    }
                    is BaseResult.Success -> {
                        state.value = ProductsFragmentState.OnProductsLoaded(it.data)
                    }
                }
            }


        }
    }
}
