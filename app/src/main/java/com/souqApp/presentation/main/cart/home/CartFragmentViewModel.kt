package com.souqApp.presentation.main.cart.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.GetCartDetailsUseCase
import com.souqApp.domain.main.cart.ResetCartUseCase
import com.souqApp.domain.main.cart.UpdateProductUseCase
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.ProductInCartEntity
import com.souqApp.domain.main.cart.entity.UpdateProductCartEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartFragmentViewModel @Inject constructor(
    private val getCartDetailsUseCase: GetCartDetailsUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val resetCartUseCase: ResetCartUseCase
) :
    ViewModel() {

    val cartDetailsLiveData: MutableLiveData<BaseResult<CartDetailsEntity, WrappedResponse<CartDetailsResponse>>> =
        MutableLiveData()

    val loading: MutableLiveData<Boolean> = MutableLiveData()


    private fun setLoading(isLoading: Boolean) {
        loading.value = isLoading
    }


    @Inject
    fun getCartDetails() {
        viewModelScope.launch {
            getCartDetailsUseCase.execute()
                .onStart {
                    setLoading(true)
                }
                .catch { setLoading(false) }.collect {
                    setLoading(false)
                    cartDetailsLiveData.value = it
                }
        }
    }

    fun resetCart(onResult: (BaseResult<String, String>) -> Unit) {
        viewModelScope.launch {
            resetCartUseCase.invoke().onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    onResult(it)
                }
        }
    }

    fun updateProduct(
        product: ProductInCartEntity,
        isIncrease: Boolean,
        onResult: (BaseResult<UpdateProductCartEntity, WrappedResponse<UpdateProductCartResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            updateProductUseCase.execute(product, isIncrease)
                .collect { onResult(it) }
        }
    }
}
