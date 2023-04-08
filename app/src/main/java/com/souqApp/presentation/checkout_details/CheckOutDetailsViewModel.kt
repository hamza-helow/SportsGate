package com.souqApp.presentation.checkout_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.checkout_details.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.domain.checkout_details.CheckoutDetailsEntity
import com.souqApp.domain.checkout_details.PaymentDetailsUseCase
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentDetailsViewModel @Inject constructor(private val paymentDetailsUseCase: PaymentDetailsUseCase) :
    ViewModel() {

    var selectedIdAddress: Int? = null
    var selectedDeliveryOptionId: Int? = null

    private val _state = MutableLiveData<PaymentDetailsActivityState>()
    val state: LiveData<PaymentDetailsActivityState> get() = _state

    private fun setLoading(loading: Boolean) {
        _state.value = PaymentDetailsActivityState.Loading(loading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = PaymentDetailsActivityState.Error(throwable)
    }


    private fun onCheckoutDetailsLoaded(checkoutDetailsEntity: CheckoutDetailsEntity) {
        _state.value = PaymentDetailsActivityState.CheckoutDetailsLoaded(checkoutDetailsEntity)
    }


    private fun onCheckoutDetailsErrorLoad(response: WrappedResponse<CheckoutDetailsResponse>) {
        _state.value = PaymentDetailsActivityState.CheckoutDetailsErrorLoad(response)
    }


    private fun onCheckCouponCode(valid: Boolean) {
        _state.value = PaymentDetailsActivityState.CheckCouponCode(valid)
    }

    private fun onCheckoutSuccess(checkoutEntity: CheckoutEntity) {
        _state.value = PaymentDetailsActivityState.CheckoutSuccess(checkoutEntity)
    }

    private fun onCheckoutError(response: WrappedResponse<CheckoutResponse>) {
        _state.value = PaymentDetailsActivityState.CheckoutError(response)
    }

    init {
        getCheckoutDetails()
    }

    fun getCheckoutDetails(deliveryOptionId: Int? = null) {
        viewModelScope.launch {
            paymentDetailsUseCase
                .getCheckoutDetails(deliveryOptionId)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onCheckoutDetailsLoaded(it.data)
                        is BaseResult.Errors -> onCheckoutDetailsErrorLoad(it.error)
                    }
                }
        }
    }

    fun checkout(couponCode: String) {
        viewModelScope.launch {
            paymentDetailsUseCase
                .checkout(
                    couponCode = couponCode,
                    addressId = selectedIdAddress,
                    deliveryOptionId = selectedDeliveryOptionId
                )
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onCheckoutSuccess(it.data)
                        is BaseResult.Errors -> onCheckoutError(it.error)
                    }
                }

        }
    }

    fun checkCouponCode(code: String) {
        viewModelScope.launch {

            paymentDetailsUseCase
                .checkCouponCode(code)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    onCheckCouponCode(it)
                }
        }
    }

}

sealed class PaymentDetailsActivityState {

    data class Loading(val loading: Boolean) : PaymentDetailsActivityState()
    data class Error(val throwable: Throwable) : PaymentDetailsActivityState()
    data class CheckoutDetailsLoaded(val checkoutDetailsEntity: CheckoutDetailsEntity) :
        PaymentDetailsActivityState()

    data class CheckoutDetailsErrorLoad(val response: WrappedResponse<CheckoutDetailsResponse>) :
        PaymentDetailsActivityState()


    data class CheckCouponCode(val valid: Boolean) : PaymentDetailsActivityState()

    data class CheckoutSuccess(val checkoutEntity: CheckoutEntity) : PaymentDetailsActivityState()
    data class CheckoutError(val response: WrappedResponse<CheckoutResponse>) :
        PaymentDetailsActivityState()


}