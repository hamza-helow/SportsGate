package com.souqApp.presentation.addresses.address_details

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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressDetailsViewModel @Inject constructor(private val addressUseCase: AddressUseCase) :
    ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val addressDetailsLiveData: MutableLiveData<BaseResult<AddressDetailsEntity, WrappedResponse<AddressDetailsResponse>>> =
        MutableLiveData()

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }

    fun getAddressDetails(addressId: Int) {
        viewModelScope.launch {
            addressUseCase.getDetails(addressId)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    addressDetailsLiveData.value = it
                }
        }
    }

}