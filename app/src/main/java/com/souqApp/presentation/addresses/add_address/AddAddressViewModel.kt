package com.souqApp.presentation.addresses.add_address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.souqApp.data.addresses.remote.dto.AddressRequest
import com.souqApp.data.addresses.remote.dto.CityResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.domain.addresses.AddressUseCase
import com.souqApp.domain.addresses.CityEntity
import com.souqApp.domain.common.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(private val addressUseCase: AddressUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<AddAddressFragmentState>()
    val state: LiveData<AddAddressFragmentState> get() = _state

    private val _userLatLng = MutableLiveData<LatLng>()
    val userLatLng: LiveData<LatLng> get() = _userLatLng


    fun setUserLatLng(latLng: LatLng) {
        _userLatLng.value = latLng
    }

    private fun setLoading(isLoading: Boolean) {
        _state.value = AddAddressFragmentState.Loading(isLoading)
    }

    private fun onUpdateAddress(updated: Boolean) {
        _state.value = AddAddressFragmentState.UpdateAddress.AddressUpdated(updated)
    }

    private fun onError(throwable: Throwable) {
        _state.value = AddAddressFragmentState.Error(throwable)
    }

    private fun onLoadedCites(cityEntities: List<CityEntity>) {
        _state.value = AddAddressFragmentState.LoadCities.CitiesLoaded(cityEntities)
    }

    private fun onAddAddress(added: Boolean) {
        _state.value = AddAddressFragmentState.AddAddress.AddedAddress(added)
    }

    private fun onErrorLoadCities(response: WrappedListResponse<CityResponse>) {
        _state.value = AddAddressFragmentState.LoadCities.CitiesErrLoad(response)
    }

    @Inject
    fun loadCities() {
        viewModelScope.launch {

            addressUseCase.getCitiesHaveAreas()
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onLoadedCites(it.data)
                        is BaseResult.Errors -> onErrorLoadCities(it.error)
                    }
                }
        }
    }


    fun addAddress(addressRequest: AddressRequest) {

        viewModelScope.launch {

            addressUseCase.add(addressRequest)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    onAddAddress(it)
                }
        }

    }


    fun updateAddress(addressRequest: AddressRequest) {

        viewModelScope.launch {

            addressUseCase
                .update(addressRequest)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    onUpdateAddress(it)
                }
        }
    }

}


sealed class AddAddressFragmentState {

    data class Loading(val isLoading: Boolean) : AddAddressFragmentState()
    data class Error(val throwable: Throwable) : AddAddressFragmentState()

    sealed class LoadCities {
        data class CitiesLoaded(val cityEntities: List<CityEntity>) : AddAddressFragmentState()
        data class CitiesErrLoad(val response: WrappedListResponse<CityResponse>) :
            AddAddressFragmentState()
    }

    sealed class AddAddress {
        data class AddedAddress(val added: Boolean) : AddAddressFragmentState()
    }

    sealed class UpdateAddress {
        data class AddressUpdated(val updated: Boolean) : AddAddressFragmentState()
    }


}