package com.souqApp.presentation.addresses.address_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.addresses.remote.dto.AddressDetailsResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.addresses.AddressDetailsEntity
import com.souqApp.domain.addresses.AddressUseCase
import com.souqApp.domain.common.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressDetailsViewModel @Inject constructor(private val addressUseCase: AddressUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<AddressDetailsFragmentState>()
    val state: LiveData<AddressDetailsFragmentState> get() = _state


    private fun setLoading(isLoading: Boolean) {
        _state.value = AddressDetailsFragmentState.Loading(isLoading)
    }

    private fun onAddressDetailsLoaded(addressDetailsEntity: AddressDetailsEntity) {
        _state.value = AddressDetailsFragmentState.AddressDetailsLoaded(addressDetailsEntity)
    }

    private fun onAddressDetailsErrorLoad(response: WrappedResponse<AddressDetailsResponse>) {
        _state.value = AddressDetailsFragmentState.AddressDetailsErrorLoad(response)
    }

    private fun onError(throwable: Throwable) {
        _state.value = AddressDetailsFragmentState.Error(throwable)
    }

    fun getAddressDetails(addressId: Int) {
        viewModelScope.launch {
            addressUseCase.getDetails(addressId)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onAddressDetailsLoaded(it.data)
                        is BaseResult.Errors -> onAddressDetailsErrorLoad(it.error)
                    }
                }
        }
    }

}

sealed class AddressDetailsFragmentState {
    data class Loading(val isLoading: Boolean) : AddressDetailsFragmentState()
    data class Error(val throwable: Throwable) : AddressDetailsFragmentState()
    data class AddressDetailsLoaded(val addressDetailsEntity: AddressDetailsEntity) :
        AddressDetailsFragmentState()

    data class AddressDetailsErrorLoad(val response: WrappedResponse<AddressDetailsResponse>) :
        AddressDetailsFragmentState()

}