package com.souqApp.presentation.main.cart.checkout_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.main.cart.remote.dto.PaymentMethodResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.usecase.CheckCouponUseCase
import com.souqApp.domain.main.cart.usecase.CheckoutUseCase
import com.souqApp.domain.main.cart.usecase.GetCheckoutDetailsUseCase
import com.souqApp.domain.main.cart.entity.CheckoutDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import com.souqApp.domain.main.cart.entity.PaymentMethodEntity
import com.souqApp.domain.main.cart.usecase.GetPaymentMethodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentDetailsViewModel @Inject constructor(
    private val getCheckoutDetailsUseCase: GetCheckoutDetailsUseCase,
    private val checkoutUseCase: CheckoutUseCase,
    private val checkCouponUseCase: CheckCouponUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase
) :
    ViewModel() {

    var defaultIdAddress: Int? = null
    var selectedIdAddress: Int? = null
    var selectedDeliveryOptionId: Int? = null

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val checkoutDetailsLiveData: MutableLiveData<BaseResult<CheckoutDetailsEntity, WrappedResponse<CheckoutDetailsResponse>>> =
        MutableLiveData()

    val paymentMethodsLiveData: MutableLiveData<BaseResult<List<PaymentMethodEntity>, WrappedListResponse<PaymentMethodResponse>>> =
        MutableLiveData()

    val checkCouponCodeLiveData: MutableLiveData<Boolean> = MutableLiveData()


    private val _validateLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val validateLiveData: LiveData<Boolean> get() = _validateLiveData


    fun validate() {
        _validateLiveData.value =
            (selectedIdAddress != null || defaultIdAddress != null) || selectedDeliveryOptionId == 2
    }

    private fun setLoading(loading: Boolean) {
        loadingLiveData.value = loading
    }

    init {
        getCheckoutDetails()
        getPaymentMethods()
    }

    private fun getPaymentMethods() {
        viewModelScope.launch {
            getPaymentMethodsUseCase.invoke()
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    paymentMethodsLiveData.value = it
                }
        }
    }

    fun getCheckoutDetails(deliveryOptionId: Int? = null) {
        viewModelScope.launch {
            getCheckoutDetailsUseCase
                .execute(deliveryOptionId)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    checkoutDetailsLiveData.value = it
                }
        }
    }

    fun checkout(
        couponCode: String,
        result: (BaseResult<CheckoutEntity, WrappedResponse<CheckoutResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            checkoutUseCase
                .execute(
                    couponCode = couponCode,
                    addressId = selectedIdAddress ?: defaultIdAddress,
                    deliveryOptionId = selectedDeliveryOptionId
                )
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    result(it)
                }

        }
    }

    fun checkCouponCode(code: String) {
        viewModelScope.launch {
            checkCouponUseCase
                .execute(code)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    checkCouponCodeLiveData.value = it
                }
        }
    }
}