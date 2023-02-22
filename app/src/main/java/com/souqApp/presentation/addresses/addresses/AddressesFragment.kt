package com.souqApp.presentation.addresses.addresses

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.R
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.databinding.FragmentAddressesBinding
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressesFragment : BaseFragment<FragmentAddressesBinding>(FragmentAddressesBinding::inflate),
    View.OnClickListener {

    private lateinit var addressAdapter: AdapterAddress
    private val viewModel: AddressViewModel by navGraphViewModels(R.id.addresses_graph) { defaultViewModelProviderFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addressAdapter = AdapterAddress()
        binding.recAddresses.layoutManager = LinearLayoutManager(requireContext())
        binding.recAddresses.adapter = addressAdapter

        addressAdapter.onClickItem = {
            navigate(AddressesFragmentDirections.toAddressDetailsFragment(it))
        }
        initListener()
        observer()
    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner) { handleState(it) }

        addressAdapter.onClickMoreButton = { address, position ->
            val bottomSheet = AddressOptionsBottomSheet(address.isPrimary)

            bottomSheet.onClickDeleteButton = {
                viewModel.deleteAddress(address.id, position)
            }

            bottomSheet.onClickChangeDefault = {
                viewModel.changeDefault(addressId = address.id)
            }

            bottomSheet.show(
                requireActivity().supportFragmentManager,
                ""
            )
        }
    }


    private fun handleState(state: AddressesFragmentState) {
        when (state) {
            is AddressesFragmentState.Init -> Unit
            is AddressesFragmentState.Loading -> handleLoading(state.isLoading)
            is AddressesFragmentState.Error -> handleError(state.throwable)
            is AddressesFragmentState.AddressesLoaded -> handleAddressesLoaded(state.addressEntities)
            is AddressesFragmentState.AddressesErrorLoad -> handleAddressesErrorLoad(state.response)
            is AddressesFragmentState.ChangeDefaultAddress -> handleChangeDefaultAddress(state.changed)
            is AddressesFragmentState.DeleteAddress ->
                handleDeleteAddress(state.deleted, state.position)
        }
    }

    private fun handleChangeDefaultAddress(changed: Boolean) {
        if (changed) {
            viewModel.getAddresses() // reload addresses
        }
    }

    private fun handleDeleteAddress(deleted: Boolean, position: Int) {
        if (deleted) {
            addressAdapter.removeItem(position)
            checkEmptyAddresses()
        }
    }

    private fun handleAddressesErrorLoad(response: WrappedListResponse<AddressResponse>) {
        requireContext().showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleAddressesLoaded(addressEntities: List<AddressEntity>) {
        addressAdapter.list = addressEntities
        checkEmptyAddresses()
    }

    private fun checkEmptyAddresses() {
        binding.cardEmptyAddress.isVisible(addressAdapter.list.isEmpty())

    }

    private fun handleError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    private fun handleLoading(loading: Boolean) {
        binding.loader.loadingProgressBar.start(loading)
    }

    private fun initListener() {
        binding.fbAddAddress.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddressesFragment()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.fbAddAddress.id -> goToAddAddressFragment()
        }
    }

    private fun goToAddAddressFragment() {
        navigate(AddressesFragmentDirections.toAddAddressFragment())
    }
}