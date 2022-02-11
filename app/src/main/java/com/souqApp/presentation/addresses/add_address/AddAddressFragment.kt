package com.souqApp.presentation.addresses.add_address

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.maps.model.LatLng
import com.souqApp.R
import com.souqApp.data.addresses.remote.dto.AddressRequest
import com.souqApp.data.addresses.remote.dto.CityResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.databinding.FragmentAddAddressBinding
import com.souqApp.domain.addresses.AddressDetailsEntity
import com.souqApp.domain.addresses.AreaEntity
import com.souqApp.domain.addresses.CityEntity
import com.souqApp.infra.extension.*
import com.souqApp.infra.utils.ADDRESS_DETAILS
import com.souqApp.infra.utils.LOCATION_USER
import com.souqApp.presentation.addresses.AddressActivityViewModel
import com.souqApp.presentation.addresses.map.MapsActivity
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException

@AndroidEntryPoint
class AddAddressFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddAddressBinding

    private val viewModel: AddAddressViewModel by viewModels()

    private lateinit var mainViewModel: AddressActivityViewModel

    private val openMapActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val latLng = result.data?.getParcelableExtra<LatLng>(LOCATION_USER)
                if (latLng != null) {
                    viewModel.setUserLatLng(latLng)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        handleBack()
        fetchAddressDetailsIfExist()

        return binding.root
    }

    // when update address
    private fun fetchAddressDetailsIfExist() {
        val addressDetails = getAddressDetails() ?: return
        binding.addressDetails = addressDetails
        viewModel.setUserLatLng(LatLng(addressDetails.lat, addressDetails.lng))
    }

    private fun handleBack() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                NavHostFragment.findNavController(this@AddAddressFragment).navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    // if update should be not null
    private fun getAddressDetails(): AddressDetailsEntity? {
        val address = arguments?.getSerializable(ADDRESS_DETAILS) ?: return null
        return address as AddressDetailsEntity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        observer()
    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner, { handleState(it) })
        viewModel.userLatLng.observe(viewLifecycleOwner, { whenUserSetLatLng() })

        activity?.run {
            mainViewModel = ViewModelProvider(this)[AddressActivityViewModel::class.java]
        } ?: throw Throwable("invalid activity")

        mainViewModel.updateActionBarTitle(getString(R.string.add_a_new_address_str))
    }

    private fun whenUserSetLatLng() {
        binding.txtPickLocation.text = getString(R.string.location_selected)
        binding.cardPickLocation.successBorder()
        validate()
    }

    private fun validate() {
        resetAllError()
        var isValid = true

        if (binding.txtBuildingNumber.text.isBlank()) {
            binding.txtBuildingNumber.errorBorder()
            isValid = false
        } else {
            binding.txtBuildingNumber.successBorder()
        }

        if (binding.txtFloorNumber.text.isBlank()) {
            binding.txtFloorNumber.errorBorder()
            isValid = false
        } else {
            binding.txtFloorNumber.successBorder()
        }

        if (binding.txtStreet.text.isBlank()) {
            binding.txtStreet.errorBorder()
            isValid = false
        } else {
            binding.txtStreet.successBorder()
        }

        binding.btnSubmit.isEnabled = isValid && viewModel.userLatLng.value != null
    }

    private fun resetAllError() {
        binding.btnSubmit.isEnabled = false
        binding.txtFloorNumber.noneBorder()
        binding.txtBuildingNumber.noneBorder()
        binding.txtStreet.noneBorder()
    }

    private fun handleState(state: AddAddressFragmentState?) {
        when (state) {
            is AddAddressFragmentState.Loading -> handleLoading(state.isLoading)
            is AddAddressFragmentState.Error -> handleError(state.throwable)
            is AddAddressFragmentState.LoadCities.CitiesLoaded -> handleCitiesLoaded(state.cityEntities)
            is AddAddressFragmentState.LoadCities.CitiesErrLoad -> handleCitiesErrLoad(state.response)
            is AddAddressFragmentState.AddAddress.AddedAddress -> handleAddOrUpdateAddress(state.added)
            is AddAddressFragmentState.UpdateAddress.AddressUpdated -> handleAddOrUpdateAddress(
                state.updated
            )
        }
    }

    private fun handleAddOrUpdateAddress(success: Boolean) {
        if (success)
            Navigation
                .findNavController(binding.root)
                .popBackStack()
    }

    private fun handleCitiesErrLoad(response: WrappedListResponse<CityResponse>) {
        binding.loader.loadingProgressBar.start(false)
        requireContext().showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleCitiesLoaded(cityEntities: List<CityEntity>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            cityEntities
        )
        binding.spinnerCities.adapter = adapter
    }

    private fun handleError(error: Throwable) {
        binding.loader.loadingProgressBar.start(false)
        if (error is SocketTimeoutException) {
            requireContext().showToast("Unexpected error, try again later")
        }
    }

    private fun handleLoading(loading: Boolean) {
        binding.loader.loadingProgressBar.start(loading)
        binding.btnSubmit.isEnabled = !loading
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
            binding.cardPickLocation.id -> goToMapActivity()
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

    private fun goToMapActivity() {
        openMapActivity.launch(Intent(requireActivity(), MapsActivity::class.java))
    }
}