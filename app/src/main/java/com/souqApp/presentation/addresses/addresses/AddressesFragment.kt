package com.souqApp.presentation.addresses.addresses

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.R
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.databinding.FragmentAddressesBinding
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.infra.utils.ID_ADDRESS
import dagger.hilt.android.AndroidEntryPoint
import com.souqApp.presentation.addresses.AddressActivityViewModel


@AndroidEntryPoint
class AddressesFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddressesBinding
    private lateinit var addressAdapter: AdapterAddress
    private lateinit var mainViewModel: AddressActivityViewModel

    private val viewModel: AddressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addressAdapter = AdapterAddress()
        binding.recAddresses.layoutManager = LinearLayoutManager(requireContext())
        binding.recAddresses.adapter = addressAdapter

        addressAdapter.onClickItem = {

            val bundle = bundleOf(ID_ADDRESS to it)
            Navigation.findNavController(requireView())
                .navigate(R.id.addressDetailsFragment, bundle)
        }

        setTitleAppBar()
        initListener()
        observer()
    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner, { handleState(it) })

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

    private fun setTitleAppBar() {
        activity?.run {
            mainViewModel = ViewModelProvider(this)[AddressActivityViewModel::class.java]
        } ?: throw Throwable("invalid activity")

        mainViewModel.updateActionBarTitle(getString(R.string.mange_addresses_str))
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAddresses()  //reloadAddresses
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
        }
    }

    private fun handleAddressesErrorLoad(response: WrappedListResponse<AddressResponse>) {
        requireContext().showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleAddressesLoaded(addressEntities: List<AddressEntity>) {
        addressAdapter.list = addressEntities
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
            binding.fbAddAddress.id -> goToAddAddressFragment(view)
        }
    }

    private fun goToAddAddressFragment(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_addressesFragment_to_addAddressFragment)
    }
}