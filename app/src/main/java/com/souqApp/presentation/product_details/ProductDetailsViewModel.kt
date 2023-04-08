package com.souqApp.presentation.product_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.product_details.GetVariationProductPriceInfoUseCase
import com.souqApp.domain.product_details.ProductDetailsUseCase
import com.souqApp.domain.product_details.VariationProductPriceInfoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productDetailsUseCase: ProductDetailsUseCase,
    private val getVariationProductPriceInfoUseCase: GetVariationProductPriceInfoUseCase
) :
    ViewModel() {

    private var isFavorite = false

    private val state = MutableLiveData<ProductDetailsActivityState>()
    val mState: LiveData<ProductDetailsActivityState> get() = state

    private var variationCombinationId: Int? = null


    private fun setLoading(isLoading: Boolean) {
        state.value = ProductDetailsActivityState.Loading(isLoading)
    }

    private fun onDetailsLoaded(productDetailsEntity: ProductDetailsEntity) {
        variationCombinationId = productDetailsEntity.variation_compaination_id

        state.value = ProductDetailsActivityState.DetailsLoaded(productDetailsEntity)
    }

    private fun onDetailsErrorLoaded(wrappedResponse: WrappedResponse<ProductDetailsEntity>) {
        state.value = ProductDetailsActivityState.DetailsErrorLoaded(wrappedResponse)
    }


    private fun onError(throwable: Throwable) {
        state.value = ProductDetailsActivityState.Error(throwable)
    }

    private fun setAddedToCart(isAdded: Boolean) {
        state.value = ProductDetailsActivityState.AddedToCart(isAdded)
    }

    private fun onAddingToCart(onProgress: Boolean) {
        state.value = ProductDetailsActivityState.AddingToCart(onProgress)
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

    fun getVariationProductPriceInfo(productId: Int, label: String) {
        viewModelScope.launch {
            getVariationProductPriceInfoUseCase.execute(productId, label).onStart {
                setLoading(true)
            }.catch {
                setLoading(false)
                onError(it)
            }.collect {
                setLoading(false)
                when (it) {
                    is BaseResult.Errors -> Unit
                    is BaseResult.Success -> {
                        variationCombinationId = it.data.combinationId
                        state.value =
                            ProductDetailsActivityState.VariationProductPriceLoaded(it.data)
                    }
                }

            }
        }
    }

    fun addProductToCart(productId: Int) {
        viewModelScope.launch {
            productDetailsUseCase
                .addProductToCart(productId, variationCombinationId)
                .onStart {
                    onAddingToCart(true)
                }
                .catch {
                    onAddingToCart(false)
                    onError(it)
                }
                .collect {
                    onAddingToCart(false)
                    when (it) {
                        true -> setAddedToCart(true)
                        false -> setAddedToCart(false)
                    }
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

    data class DetailsErrorLoaded(val wrappedResponse: WrappedResponse<ProductDetailsEntity>) :
        ProductDetailsActivityState()

    data class Error(val throwable: Throwable) : ProductDetailsActivityState()

    data class ToggleFavorite(val isFavorite: Boolean) : ProductDetailsActivityState()

    data class AddedToCart(val isAdded: Boolean) : ProductDetailsActivityState()

    data class AddingToCart(val inProgress: Boolean) : ProductDetailsActivityState()

    data class VariationProductPriceLoaded(val variationProductPriceInfoEntity: VariationProductPriceInfoEntity) :
        ProductDetailsActivityState()

}