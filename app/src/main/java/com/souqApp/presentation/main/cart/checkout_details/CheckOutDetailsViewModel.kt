package com.souqApp.presentation.main.cart.checkout_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.CheckCouponUseCase
import com.souqApp.domain.main.cart.CheckoutUseCase
import com.souqApp.domain.main.cart.GetCheckoutDetailsUseCase
import com.souqApp.domain.main.cart.entity.CheckoutDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentDetailsViewModel @Inject constructor(
    private val getCheckoutDetailsUseCase: GetCheckoutDetailsUseCase,
    private val checkoutUseCase: CheckoutUseCase,
    private val checkCouponUseCase: CheckCouponUseCase
) :
    ViewModel() {

    var defaultIdAddress: Int? = null
    var selectedIdAddress: Int? = null
    var selectedDeliveryOptionId: Int? = null

    private val _state = MutableLiveData<PaymentDetailsFragmentState>()
    val state: LiveData<PaymentDetailsFragmentState> get() = _state

    private val _validateLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val validateLiveData: LiveData<Boolean> get() = _validateLiveData


    fun validate() {
        _validateLiveData.value =
            (selectedIdAddress != null || defaultIdAddress != null) || selectedDeliveryOptionId == 2
    }

    private fun setLoading(loading: Boolean) {
        _state.value = PaymentDetailsFragmentState.Loading(loading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = PaymentDetailsFragmentState.Error(throwable)
    }


    private fun onCheckoutDetailsLoaded(checkoutDetailsEntity: CheckoutDetailsEntity) {
        _state.value = PaymentDetailsFragmentState.CheckoutDetailsLoaded(checkoutDetailsEntity)
    }


    private fun onCheckoutDetailsErrorLoad(response: WrappedResponse<CheckoutDetailsResponse>) {
        _state.value = PaymentDetailsFragmentState.CheckoutDetailsErrorLoad(response)
    }


    private fun onCheckCouponCode(valid: Boolean) {
        _state.value = PaymentDetailsFragmentState.CheckCouponCode(valid)
    }

    private fun onCheckoutSuccess(checkoutEntity: CheckoutEntity) {
        _state.value = PaymentDetailsFragmentState.CheckoutSuccess(checkoutEntity)
    }

    private fun onCheckoutError(response: WrappedResponse<CheckoutResponse>) {
        _state.value = PaymentDetailsFragmentState.CheckoutError(response)
    }

    init {
        getCheckoutDetails()
    }

    fun getCheckoutDetails(deliveryOptionId: Int? = null) {
        viewModelScope.launch {
            getCheckoutDetailsUseCase
                .execute(deliveryOptionId)
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
            checkoutUseCase
                .execute(
                    couponCode = couponCode,
                    addressId = selectedIdAddress ?: defaultIdAddress,
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
            checkCouponUseCase
                .execute(code)
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

sealed class PaymentDetailsFragmentState {

    data class Loading(val loading: Boolean) : PaymentDetailsFragmentState()
    data class Error(val throwable: Throwable) : PaymentDetailsFragmentState()
    data class CheckoutDetailsLoaded(val checkoutDetailsEntity: CheckoutDetailsEntity) :
        PaymentDetailsFragmentState()

    data class CheckoutDetailsErrorLoad(val response: WrappedResponse<CheckoutDetailsResponse>) :
        PaymentDetailsFragmentState()


    data class CheckCouponCode(val valid: Boolean) : PaymentDetailsFragmentState()

    data class CheckoutSuccess(val checkoutEntity: CheckoutEntity) : PaymentDetailsFragmentState()
    data class CheckoutError(val response: WrappedResponse<CheckoutResponse>) :
        PaymentDetailsFragmentState()
}