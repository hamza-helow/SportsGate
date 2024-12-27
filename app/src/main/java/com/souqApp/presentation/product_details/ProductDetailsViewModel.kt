package com.souqApp.presentation.product_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.products.remote.dto.AddProductToCartResponse
import com.souqApp.data.products.remote.dto.AddToFavoriteResponse
import com.souqApp.data.products.remote.dto.ProductDetailsEntity
import com.souqApp.data.products.remote.dto.ProductDetailsResponse
import com.souqApp.data.products.remote.dto.VariationProductPriceInfoResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.entity.AddProductToCartEntity
import com.souqApp.domain.products.GetVariationProductPriceInfoUseCase
import com.souqApp.domain.products.usecase.ProductDetailsUseCase
import com.souqApp.domain.products.entity.VariationProductPriceInfoEntity
import com.souqApp.infra.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productDetailsUseCase: ProductDetailsUseCase,
    private val getVariationProductPriceInfoUseCase: GetVariationProductPriceInfoUseCase,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    val productId by lazy { savedStateHandle.get<Int>(Constant.PRODUCT_ID) }
    var variationCombinationId: Int? = null
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val variationProductPriceLiveData: MutableLiveData<BaseResult<VariationProductPriceInfoEntity, WrappedResponse<VariationProductPriceInfoResponse>>> =
        MutableLiveData()

    val productDetailsLiveData: MutableLiveData<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsResponse>>> =
        MutableLiveData()

    private val addingToCartLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getProductDetails()
    }

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

    fun getProductDetails() {
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
