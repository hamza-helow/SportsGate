package com.souqApp.presentation.main.cart.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.main.cart.GetCartDetailsUseCase
import com.souqApp.domain.main.cart.ResetCartUseCase
import com.souqApp.domain.main.cart.UpdateProductUseCase
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.ProductInCartEntity
import com.souqApp.domain.main.cart.entity.UpdateProductCartEntity
import com.souqApp.domain.users.SendOtpUseCase
import com.souqApp.presentation.common.enums.VerificationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartFragmentViewModel @Inject constructor(
    private val getCartDetailsUseCase: GetCartDetailsUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val resetCartUseCase: ResetCartUseCase,
    private val sendOtpUseCase: SendOtpUseCase,
) :
    ViewModel() {

    val cartDetailsLiveData: MutableLiveData<BaseResult<CartDetailsEntity, WrappedResponse<CartDetailsResponse>>> =
        MutableLiveData()

    val loadingCart: MutableLiveData<Boolean> = MutableLiveData()
    val loadingVerifyMethod: MutableLiveData<Boolean> = MutableLiveData()


    private fun setLoadingCart(isLoading: Boolean) {
        loadingCart.value = isLoading
    }

    private fun setLoadingVerifyMethod(isLoading: Boolean) {
        loadingVerifyMethod.value = isLoading
    }

    @Inject
    fun getCartDetails() {
        viewModelScope.launch {
            getCartDetailsUseCase.execute()
                .onStart {
                    setLoadingCart(true)
                }
                .catch { setLoadingCart(false) }.collect {
                    setLoadingCart(false)
                    cartDetailsLiveData.value = it
                }
        }
    }

    fun sendOtpToVerifyMethod(
        verifyType: VerificationType,
        onCollect: (BaseResult<EmptyEntity, WrappedResponse<Nothing>>) -> Unit
    ) {
        viewModelScope.launch {
            sendOtpUseCase.invoke(verifyType == VerificationType.BY_PHONE)
                .onStart {
                    setLoadingVerifyMethod(true)
                }.catch {
                    setLoadingVerifyMethod(false)
                }
                .collect { result ->
                    setLoadingVerifyMethod(false)
                    onCollect(result)
                }
        }

    }


    fun resetCart(onResult: (BaseResult<String, String>) -> Unit) {
        viewModelScope.launch {
            resetCartUseCase.invoke().onStart { setLoadingCart(true) }
                .catch { setLoadingCart(false) }
                .collect {
                    setLoadingCart(false)
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
