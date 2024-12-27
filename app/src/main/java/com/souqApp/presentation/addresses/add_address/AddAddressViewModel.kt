package com.souqApp.presentation.addresses.add_address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.souqApp.data.addresses.remote.dto.AddressRequest
import com.souqApp.data.addresses.remote.dto.CityResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.domain.addresses.CityEntity
import com.souqApp.domain.addresses.usecase.AddAddressUseCase
import com.souqApp.domain.addresses.usecase.GetCitiesHavAreasUseCase
import com.souqApp.domain.common.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val updateAddAddressUseCase: AddAddressUseCase,
    private val addAddressUseCase: AddAddressUseCase,
    private val getCitiesHavAreasUseCase: GetCitiesHavAreasUseCase
) :
    ViewModel() {


    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val validate: MutableLiveData<Boolean> = MutableLiveData()
    val citiesLiveData: MutableLiveData<BaseResult<List<CityEntity>, WrappedListResponse<CityResponse>>> =
        MutableLiveData()


    private val _selectedLocation = MutableLiveData<LatLng>()
    val selectedLocation: LiveData<LatLng> get() = _selectedLocation

    fun validate(street: String, buildingNumber: String, floorNumber: String) {
        validate.value =
            street.isNotBlank() && buildingNumber.isNotBlank()
                    && floorNumber.isNotBlank() && selectedLocation.value != null
    }

    fun setSelectedLocation(latLng: LatLng) {
        _selectedLocation.value = latLng
    }

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }

    @Inject
    fun loadCities() {
        viewModelScope.launch {
            getCitiesHavAreasUseCase.invoke()
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    citiesLiveData.value = it
                }
        }
    }


    fun addAddress(addressRequest: AddressRequest, onResult: (added: Boolean) -> Unit) {
        viewModelScope.launch {
            addAddressUseCase.invoke(addressRequest)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    onResult(it)
                }
        }

    }


    fun updateAddress(addressRequest: AddressRequest, onResult: (updated: Boolean) -> Unit) {
        viewModelScope.launch {
            updateAddAddressUseCase
                .invoke(addressRequest)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    onResult(it)
                }
        }
    }

}
