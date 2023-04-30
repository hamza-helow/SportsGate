package com.souqApp.presentation.product_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.product_details.AddProductToCartEntity
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

    private var variationCombinationId: Int? = null
    private val state = MutableLiveData<ProductDetailsActivityState>()
    val mState: LiveData<ProductDetailsActivityState> get() = state

    private fun setLoading(isLoading: Boolean) {
        state.value = ProductDetailsActivityState.Loading(isLoading)
    }

    private fun onDetailsLoaded(productDetailsEntity: ProductDetailsEntity) {
        variationCombinationId = productDetailsEntity.variationCompainationId

        state.value = ProductDetailsActivityState.DetailsLoaded(productDetailsEntity)
    }

    private fun setOnError(wrappedResponse: WrappedResponse<*>) {
        state.value = ProductDetailsActivityState.DetailsErrorLoaded(wrappedResponse)
    }


    private fun setAddedToCart(entity: AddProductToCartEntity) {
        state.value = ProductDetailsActivityState.AddedToCart(entity)
    }


    private fun onAddingToCart(onProgress: Boolean) {
        state.value = ProductDetailsActivityState.AddingToCart(onProgress)
    }

    private fun handleToggleFavorite(isFavorite: Boolean) {
        state.value = ProductDetailsActivityState.ToggleFavorite(isFavorite)
    }

    fun toggleFavorite(idProduct: Int) {
        viewModelScope.launch {
            productDetailsUseCase.addOrRemoveProduct(idProduct, variationCombinationId)
                .catch {}.collect {
                    when (it) {
                        is BaseResult.Errors -> Unit
                        is BaseResult.Success -> handleToggleFavorite(it.data.userFavourite)
                    }
                }
        }
    }

    fun getVariationProductPriceInfo(productId: Int, label: String) {
        viewModelScope.launch {
            getVariationProductPriceInfoUseCase.execute(productId, label).onStart {
                setLoading(true)
            }.catch {
                setLoading(false)
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
                }
                .collect {
                    onAddingToCart(false)
                    when (it) {
                        is BaseResult.Errors -> setOnError(it.error)
                        is BaseResult.Success -> setAddedToCart(it.data)
                    }
                }
        }
    }

    fun productDetails(productId: Int) {
        viewModelScope.launch {
            productDetailsUseCase.productDetails(productId)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }.collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onDetailsLoaded(it.data)
                        is BaseResult.Errors -> setOnError(it.error)
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

    data class DetailsErrorLoaded(val wrappedResponse: WrappedResponse<*>) :
        ProductDetailsActivityState()

    data class ToggleFavorite(val isFavorite: Boolean) : ProductDetailsActivityState()

    data class AddedToCart(val entity: AddProductToCartEntity) : ProductDetailsActivityState()

    data class AddingToCart(val inProgress: Boolean) : ProductDetailsActivityState()

    data class VariationProductPriceLoaded(val variationProductPriceInfoEntity: VariationProductPriceInfoEntity) :
        ProductDetailsActivityState()

}