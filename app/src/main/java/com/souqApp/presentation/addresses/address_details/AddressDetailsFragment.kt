package com.souqApp.presentation.addresses.address_details

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.souqApp.R
import com.souqApp.data.addresses.remote.dto.AddressDetailsResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.databinding.FragmentAddressDetailsBinding
import com.souqApp.domain.addresses.AddressDetailsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.ADDRESS_DETAILS
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressDetailsFragment :
    BaseFragment<FragmentAddressDetailsBinding>(FragmentAddressDetailsBinding::inflate),
    OnMapReadyCallback, View.OnClickListener {

    private val viewModel: AddressDetailsViewModel by viewModels()
    private lateinit var mMap: GoogleMap

    private val args: AddressDetailsFragmentArgs by navArgs()

    private fun initMap(savedInstanceState: Bundle?) {
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)
        binding.map.onResume() // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToLoading()
        observeToAddressDetails()
        handleBack()
        initMap(savedInstanceState)
        initListener()
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun observeToAddressDetails() {
        viewModel.addressDetailsLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is BaseResult.Errors -> handleAddressDetailsErrorLoad(result.error)
                is BaseResult.Success -> handleAddressDetailsLoaded(result.data)
            }
        }
    }

    private fun initListener() {
        binding.btnEdit.setOnClickListener(this)
    }

    private fun handleAddressDetailsErrorLoad(response: WrappedResponse<AddressDetailsResponse>) {
        showDialog(response.message)
    }

    private fun handleLoading(loading: Boolean) {
        binding.progressBar.start(loading)
        binding.content.isVisible(!loading)
    }

    private fun handleAddressDetailsLoaded(addressDetailsEntity: AddressDetailsEntity) {
        binding.addressDetails = addressDetailsEntity

        val latLng = LatLng(addressDetailsEntity.lat, addressDetailsEntity.lng)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        mMap.clear()
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16f)
        mMap.animateCamera(cameraUpdate)

        mMap.addMarker(markerOptions)
    }

    private fun handleBack() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                NavHostFragment.findNavController(this@AddressDetailsFragment).navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        viewModel.getAddressDetails(args.addressId)
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.btnEdit.id -> navigateToAddAddressFragment(view)
        }
    }

    private fun navigateToAddAddressFragment(view: View) {
        val bundle = bundleOf(ADDRESS_DETAILS to binding.addressDetails)
        Navigation.findNavController(view).navigate(R.id.addAddressFragment, bundle)
    }
}