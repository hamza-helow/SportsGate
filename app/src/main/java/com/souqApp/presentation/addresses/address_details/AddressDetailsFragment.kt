package com.souqApp.presentation.addresses.address_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.ADDRESS_DETAILS
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException


@AndroidEntryPoint
class AddressDetailsFragment : Fragment(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var binding: FragmentAddressDetailsBinding
    private val viewModel: AddressDetailsViewModel by viewModels()
    private lateinit var mMap: GoogleMap

    private val args:AddressDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressDetailsBinding.inflate(inflater, container, false)
        handleBack()
        initMap(savedInstanceState)
        return binding.root
    }


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
        viewModel.getAddressDetails(args.addressId)
        initListener()
        observer()
    }

    private fun initListener() {
        binding.btnEdit.setOnClickListener(this)
    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner) { handleState(it) }
    }

    private fun handleState(state: AddressDetailsFragmentState) {
        when (state) {
            is AddressDetailsFragmentState.Loading -> handleLoading(state.isLoading)
            is AddressDetailsFragmentState.AddressDetailsLoaded -> handleAddressDetailsLoaded(state.addressDetailsEntity)
            is AddressDetailsFragmentState.Error -> handleError(state.throwable)
            is AddressDetailsFragmentState.AddressDetailsErrorLoad -> handleAddressDetailsErrorLoad(
                state.response
            )
        }
    }

    private fun handleAddressDetailsErrorLoad(response: WrappedResponse<AddressDetailsResponse>) {
        requireContext().showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleError(error: Throwable) {
        binding.progressBar.start(false)
        binding.content.isVisible(false)

        if (error is SocketTimeoutException) {
            requireContext().showToast("Unexpected error, try again later")
        }
    }

    private fun handleLoading(loading: Boolean) {
        binding.progressBar.start(loading)
        binding.content.isVisible(!loading)
    }

    private fun handleAddressDetailsLoaded(addressDetailsEntity: AddressDetailsEntity) {
        binding.addressDetails = addressDetailsEntity

        //init latLng
        val latLng = LatLng(addressDetailsEntity.lat, addressDetailsEntity.lng)

        // Creating a marker
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)

        // Clears the previously touched position
        mMap.clear()

        // init CameraUpdateFactory
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16f)

        // Animating to the touched position
        mMap.animateCamera(cameraUpdate)

        // add marker
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

    companion object {
        @JvmStatic
        fun newInstance() = AddressDetailsFragment()
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
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