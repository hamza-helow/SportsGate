package com.souqApp.presentation.payment_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.payment_details.remote.dto.CheckoutDetailsResponse
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import com.souqApp.domain.payment_details.CheckoutDetailsEntity
import com.souqApp.domain.payment_details.PaymentDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentDetailsViewModel @Inject constructor(private val paymentDetailsUseCase: PaymentDetailsUseCase) :
    ViewModel() {

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

    private fun onAddressesLoaded(addressEntities: List<AddressEntity>) {
        _state.value = PaymentDetailsActivityState.AddressesLoaded(addressEntities)
    }

    private fun onAddressErrorLoad(response: WrappedListResponse<AddressResponse>) {
        _state.value = PaymentDetailsActivityState.AddressesErrorLoad(response)
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


    @Inject
    fun getCheckoutDetails() {
        viewModelScope.launch {
            paymentDetailsUseCase
                .getCheckoutDetails()
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

    @Inject
    fun getAddresses() {
        viewModelScope.launch {
            paymentDetailsUseCase.getAddresses()
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onAddressesLoaded(it.data)
                        is BaseResult.Errors -> onAddressErrorLoad(it.error)
                    }
                }
        }
    }

    fun checkout(checkoutRequest: CheckoutRequest) {

        viewModelScope.launch {
            paymentDetailsUseCase
                .checkout(checkoutRequest)
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

    data class AddressesLoaded(val addressEntities: List<AddressEntity>) :
        PaymentDetailsActivityState()

    data class AddressesErrorLoad(val response: WrappedListResponse<AddressResponse>) :
        PaymentDetailsActivityState()

    data class CheckCouponCode(val valid: Boolean) : PaymentDetailsActivityState()

    data class CheckoutSuccess(val checkoutEntity: CheckoutEntity) : PaymentDetailsActivityState()
    data class CheckoutError(val response: WrappedResponse<CheckoutResponse>) :
        PaymentDetailsActivityState()


}