package com.souqApp.presentation.product_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.AddProductToCartResponse
import com.souqApp.data.product_details.remote.AddToFavoriteResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.data.product_details.remote.ProductDetailsResponse
import com.souqApp.data.product_details.remote.VariationProductPriceInfoResponse
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

    var variationCombinationId: Int? = null
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val variationProductPriceLiveData: MutableLiveData<BaseResult<VariationProductPriceInfoEntity, WrappedResponse<VariationProductPriceInfoResponse>>> =
        MutableLiveData()

    val productDetailsLiveData: MutableLiveData<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsResponse>>> =
        MutableLiveData()

    val addingToCartLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }

    fun toggleFavorite(
        idProduct: Int,
        onResult: (BaseResult<AddToFavoriteResponse, WrappedResponse<AddToFavoriteResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            productDetailsUseCase.addOrRemoveProduct(idProduct, variationCombinationId)
                .catch {}
                .collect { onResult(it) }
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
                variationProductPriceLiveData.value = it
            }
        }
    }

    private fun onAddingToCart(add: Boolean) {
        addingToCartLiveData.value = add
    }

    fun addProductToCart(
        productId: Int,
        onResult: (BaseResult<AddProductToCartEntity, WrappedResponse<AddProductToCartResponse>>) -> Unit
    ) {
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
                    onResult(it)
                }
        }
    }

    fun productDetails(productId: Int) {
        viewModelScope.launch {
            productDetailsUseCase.productDetails(productId)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                }.collect {
                    setLoading(false)
                    productDetailsLiveData.value = it
                }
        }


    }
}
