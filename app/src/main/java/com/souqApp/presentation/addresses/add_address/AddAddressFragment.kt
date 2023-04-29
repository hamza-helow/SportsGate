package com.souqApp.presentation.addresses.add_address

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.maps.model.LatLng
import com.souqApp.R
import com.souqApp.data.addresses.remote.dto.AddressRequest
import com.souqApp.data.addresses.remote.dto.CityResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.databinding.FragmentAddAddressBinding
import com.souqApp.domain.addresses.AddressDetailsEntity
import com.souqApp.domain.addresses.AreaEntity
import com.souqApp.domain.addresses.CityEntity
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.successBorder
import com.souqApp.infra.utils.ADDRESS_DETAILS
import com.souqApp.presentation.addresses.map.MapsActivity
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException

@AndroidEntryPoint
class AddAddressFragment :
    BaseFragment<FragmentAddAddressBinding>(FragmentAddAddressBinding::inflate),
    View.OnClickListener {

    private val requestLocationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted)
                openMapActivity.launch(Intent(requireActivity(), MapsActivity::class.java))
        }

    private val viewModel: AddAddressViewModel by viewModels()

    private val openMapActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val lat = result.data?.getDoubleExtra(MapsActivity.LAT, 0.0) ?: 0.0
                val lng = result.data?.getDoubleExtra(MapsActivity.LNG, 0.0) ?: 0.0
                viewModel.setUserLatLng(LatLng(lat, lng))
            }
        }

    private fun fetchAddressDetailsIfExist() {
        val addressDetails = getAddressDetails() ?: return
        binding.addressDetails = addressDetails
        viewModel.setUserLatLng(LatLng(addressDetails.lat, addressDetails.lng))
    }

    private fun getAddressDetails(): AddressDetailsEntity? {
        val address = arguments?.getSerializable(ADDRESS_DETAILS) ?: return null
        return address as AddressDetailsEntity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initListener()
        fetchAddressDetailsIfExist()
        observer()
    }

    private fun init() {
        binding.txtStreet.doAfterTextChanged { validate() }
    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner) { handleState(it) }
        viewModel.userLatLng.observe(viewLifecycleOwner) { whenUserSetLatLng() }
    }

    private fun whenUserSetLatLng() {
        binding.txtPickLocation.text = getString(R.string.location_selected)
        binding.cardPickLocation.successBorder()
        validate()
    }

    private fun validate() {
        viewModel.validate(
            street = binding.txtStreet.text.toString(),
            buildingNumber = binding.txtBuildingNumber.text.toString(),
            floorNumber = binding.txtFloorNumber.text.toString()
        )
    }


    private fun handleState(state: AddAddressFragmentState) {
        when (state) {
            is AddAddressFragmentState.Loading -> handleLoading(state.isLoading)
            is AddAddressFragmentState.Error -> handleError(state.throwable)
            is AddAddressFragmentState.LoadCities.CitiesLoaded -> handleCitiesLoaded(state.cityEntities)
            is AddAddressFragmentState.LoadCities.CitiesErrLoad -> handleCitiesErrLoad(state.response)
            is AddAddressFragmentState.AddAddress.AddedAddress -> handleAddOrUpdateAddress(state.added)
            is AddAddressFragmentState.UpdateAddress.AddressUpdated -> handleAddOrUpdateAddress(
                state.updated
            )
            is AddAddressFragmentState.Validate -> handleValidate(state.isValid)
        }
    }

    private fun handleValidate(valid: Boolean) {
        binding.btnSubmit.isEnabled = valid
    }

    private fun handleAddOrUpdateAddress(success: Boolean) {
        if (success)
            Navigation.findNavController(binding.root).popBackStack()
    }

    private fun handleCitiesErrLoad(response: WrappedListResponse<CityResponse>) {
        showDialog(response.message)
    }

    private fun handleCitiesLoaded(cityEntities: List<CityEntity>) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            cityEntities
        )
        binding.spinnerCities.adapter = adapter
    }

    private fun handleError(error: Throwable) {
        if (error is SocketTimeoutException) {
            requireContext().showToast("Unexpected error, try again later")
        }
    }

    private fun handleLoading(loading: Boolean) {
        showLoading(loading)
    }

    private fun initListener() {
        binding.cardPickLocation.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.txtStreet.doAfterTextChanged { validate() }
        binding.txtBuildingNumber.doAfterTextChanged { validate() }
        binding.txtFloorNumber.doAfterTextChanged { validate() }

        binding.spinnerCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                val data = binding.spinnerCities.selectedItem as CityEntity
                val adapterAreas = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    data.areas
                )
                binding.spinnerAreas.adapter = adapterAreas
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddAddressFragment()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.cardPickLocation.id -> requestLocationPermission()
            binding.btnSubmit.id -> addOrUpdateAddress()
        }
    }

    private fun addOrUpdateAddress() {
        val buildingNumber = binding.txtBuildingNumber.text.toString()
        val floorNumber = binding.txtFloorNumber.text.toString()
        val notes = binding.txtNotes.text.toString()
        val street = binding.txtStreet.text.toString()
        val idCity = (binding.spinnerCities.selectedItem as CityEntity).id
        val idArea = (binding.spinnerAreas.selectedItem as AreaEntity).id
        val lat = viewModel.userLatLng.value?.latitude!!
        val lng = viewModel.userLatLng.value?.longitude!!
        val addressId = if (getAddressDetails() == null) null else getAddressDetails()?.id

        val addressRequest = AddressRequest(
            addressId, buildingNumber, street, floorNumber, notes, idArea, idCity, lat, lng
        )

        if (addressId == null) {
            viewModel.addAddress(addressRequest)
        } else {
            viewModel.updateAddress(addressRequest)
        }
    }

    private fun requestLocationPermission() {
        requestLocationPermissions.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}