package com.souqApp.presentation.products_by_type

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeRequest
import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products_by_type.ProductsByTypeEntity
import com.souqApp.domain.products_by_type.ProductsByTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsByTypeVM @Inject constructor(val productsByTypeUseCase: ProductsByTypeUseCase) :
    ViewModel() {

    private val state =
        MutableLiveData<ProductsByTypeActivityState>(ProductsByTypeActivityState.Init)

    val mState: LiveData<ProductsByTypeActivityState> get() = state


    private fun setLoading(isLoading: Boolean) {
        state.value = ProductsByTypeActivityState.Loading(isLoading)
    }

    private fun setError(throwable: Throwable) {
        state.value = ProductsByTypeActivityState.OnError(throwable)
    }

    private fun setOnProductsLoaded(productsByTypeEntity: ProductsByTypeEntity) {
        state.value = ProductsByTypeActivityState.ProductsLoaded(productsByTypeEntity)
    }

    private fun setOnProductsLoadedError(productsByTypeResponse: WrappedResponse<ProductsByTypeResponse>) {
        state.value = ProductsByTypeActivityState.ProductsErrorLoaded(productsByTypeResponse)
    }


    fun loadProducts(productsByTypeRequest: ProductsByTypeRequest) {
        viewModelScope.launch {
            productsByTypeUseCase
                .getProductsByType(productsByTypeRequest)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    setError(it)
                }.collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> setOnProductsLoaded(it.data)
                        is BaseResult.Errors -> setOnProductsLoadedError(it.error)
                    }
                }

        }
    }

}


sealed class ProductsByTypeActivityState {

    object Init : ProductsByTypeActivityState()
    data class Loading(val isLoading: Boolean) : ProductsByTypeActivityState()
    data class OnError(val throwable: Throwable) : ProductsByTypeActivityState()
    data class ProductsLoaded(val productsByTypeEntity: ProductsByTypeEntity) :
        ProductsByTypeActivityState()

    data class ProductsErrorLoaded(val productsByTypeResponse: WrappedResponse<ProductsByTypeResponse>) :
        ProductsByTypeActivityState()

}