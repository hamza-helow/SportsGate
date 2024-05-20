package com.souqApp.presentation.addresses.addresses

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

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val addressLiveData: MutableLiveData<BaseResult<List<AddressEntity>, WrappedListResponse<AddressResponse>>> =
        MutableLiveData()

    private fun setLoading(loading: Boolean) {
        loadingLiveData.value = loading
    }

    fun getAddresses() {
        viewModelScope.launch {
            addressUseCase.getAll()
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    addressLiveData.value = it
                }
        }
    }

    fun deleteAddress(
        addressId: Int,
        position: Int,
        onResult: (deleted: Boolean, position: Int) -> Unit
    ) {

        viewModelScope.launch {
            addressUseCase.delete(addressId)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    onResult(it, position)
                }
        }
    }


    fun changeDefault(addressId: Int, onResult: (changed: Boolean) -> Unit) {
        viewModelScope.launch {
            addressUseCase
                .changeDefault(addressId)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    onResult(it)
                }
        }
    }

}

