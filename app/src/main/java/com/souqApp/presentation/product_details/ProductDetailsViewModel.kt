package com.souqApp.presentation.product_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.product_details.ProductDetailsEntity
import com.souqApp.domain.product_details.ProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val productDetailsUseCase: ProductDetailsUseCase) :
    ViewModel() {

    private var isFavorite = false

    private val state = MutableLiveData<ProductDetailsActivityState>()
    val mState: LiveData<ProductDetailsActivityState> get() = state


    private fun setLoading(isLoading: Boolean) {
        state.value = ProductDetailsActivityState.Loading(isLoading)
    }

    private fun onDetailsLoaded(productDetailsEntity: ProductDetailsEntity) {
        state.value = ProductDetailsActivityState.DetailsLoaded(productDetailsEntity)
    }

    private fun onDetailsErrorLoaded(wrappedResponse: WrappedResponse<ProductDetailsResponse>) {
        state.value = ProductDetailsActivityState.DetailsErrorLoaded(wrappedResponse)
    }


    private fun onError(throwable: Throwable) {
        state.value = ProductDetailsActivityState.Error(throwable)
    }

    private fun handleToggleFavorite() {
        isFavorite = !isFavorite
        state.value = ProductDetailsActivityState.ToggleFavorite(isFavorite)
    }

    fun toggleFavorite(idProduct: Int) {

        viewModelScope.launch {
            productDetailsUseCase.addOrRemoveProduct(idProduct)
                .catch {
                    onError(it)
                }.collect {
                    handleToggleFavorite()
                }
        }
    }

    fun productDetails(productId: Int) {
        viewModelScope.launch {
            productDetailsUseCase.productDetails(productId)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }.collect {
                    setLoading(false)

                    when (it) {
                        is BaseResult.Success -> onDetailsLoaded(it.data)
                        is BaseResult.Errors -> onDetailsErrorLoaded(it.error)
                    }
                }
        }


    }

}


sealed class ProductDetailsActivityState {
    object Init : ProductDetailsActivityState()
    data class Loading(val isLoading: Boolean) : ProductDetailsActivityState()
    data class DetailsLoaded(val productDetailsEntity: ProductDetailsEntity) :
        ProductDetailsActivityState()

    data class DetailsErrorLoaded(val wrappedResponse: WrappedResponse<ProductDetailsResponse>) :
        ProductDetailsActivityState()

    data class Error(val throwable: Throwable) : ProductDetailsActivityState()

    data class ToggleFavorite(val isFavorite: Boolean) : ProductDetailsActivityState()

}