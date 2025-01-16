package com.souqApp.presentation.orders.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.orders.remote.dto.OrderDetailsResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.orders.entity.OrderDetailsEntity
import com.souqApp.domain.orders.OrderDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(private val orderDetailsUseCase: OrderDetailsUseCase) :
    ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val orderDetailsLiveData: MutableLiveData<BaseResult<OrderDetailsEntity, WrappedResponse<OrderDetailsResponse>>> =
        MutableLiveData()

    var href: String = ""

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }

    fun getOrderDetails(orderId: Int) {
        viewModelScope.launch {

            orderDetailsUseCase
                .getOrderDetails(orderId)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    orderDetailsLiveData.value = it
                }
        }
    }
}