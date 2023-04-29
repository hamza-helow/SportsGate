package com.souqApp.presentation.addresses.addresses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.domain.addresses.AddressUseCase
import com.souqApp.domain.common.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(private val addressUseCase: AddressUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<AddressesFragmentState>()

    val state: LiveData<AddressesFragmentState> get() = _state

    private fun setLoading(loading: Boolean) {
        _state.value = AddressesFragmentState.Loading(loading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = AddressesFragmentState.Error(throwable)
    }

    private fun onAddressesLoaded(addressEntities: List<AddressEntity>) {
        _state.value = AddressesFragmentState.AddressesLoaded(addressEntities)
    }

    private fun onAddressErrorLoad(response: WrappedListResponse<AddressResponse>) {
        _state.value = AddressesFragmentState.AddressesErrorLoad(response)
    }

    private fun onDeleteAddress(deleted: Boolean, position: Int) {
        _state.value = AddressesFragmentState.DeleteAddress(deleted, position)
    }

    private fun onChangeDefault(changed: Boolean) {
        _state.value = AddressesFragmentState.ChangeDefaultAddress(changed)
    }

    fun getAddresses() {
        viewModelScope.launch {
            addressUseCase.getAll()
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

    fun deleteAddress(addressId: Int, position: Int) {

        viewModelScope.launch {

            addressUseCase.delete(addressId)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    onDeleteAddress(it, position)
                }
        }
    }


    fun changeDefault(addressId: Int) {
        viewModelScope.launch {
            addressUseCase
                .changeDefault(addressId)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    onChangeDefault(it)
                }
        }
    }

}

sealed class AddressesFragmentState {
    object Init : AddressesFragmentState()

    data class Loading(val isLoading: Boolean) : AddressesFragmentState()
    data class Error(val throwable: Throwable) : AddressesFragmentState()
    data class AddressesLoaded(val addressEntities: List<AddressEntity>) : AddressesFragmentState()
    data class AddressesErrorLoad(val response: WrappedListResponse<AddressResponse>) :
        AddressesFragmentState()

    data class DeleteAddress(val deleted: Boolean, val position: Int) : AddressesFragmentState()

    data class ChangeDefaultAddress(val changed: Boolean) : AddressesFragmentState()
}