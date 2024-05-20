package com.souqApp.presentation.addresses.addresses

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.souqApp.R
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.databinding.FragmentAddressesBinding
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.infra.extension.setupMenu
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressesFragment :
    BaseFragment<FragmentAddressesBinding>(FragmentAddressesBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener {

    private val args: AddressesFragmentArgs by navArgs()
    private lateinit var addressAdapter: AdapterAddress
    private val viewModel: AddressViewModel by navGraphViewModels(R.id.addresses_graph) { defaultViewModelProviderFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.refreshSwiper.setOnRefreshListener(this)
        observeToLoading()
        initAdapter()
        initAddressOptionsBottomSheet()
        initMenu()
        observeToAddresses()
    }

    private fun observeToAddresses() {
        viewModel.getAddresses()
        viewModel.addressLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is BaseResult.Errors -> handleAddressesErrorLoad(result.error)
                is BaseResult.Success -> handleAddressesLoaded(result.data)
            }
        }
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun initMenu() {
        setupMenu(R.menu.menu_add) {
            if (it.itemId == R.id.item_add)
                goToAddAddressFragment()
        }
    }

    private fun initAdapter() {

        addressAdapter = AdapterAddress()
        binding.recAddresses.setAdapter(addressAdapter, LinearLayoutManager(requireContext()))

        addressAdapter.onClickItem = {
            if (args.selectedMode) {
                setFragmentResult(
                    AddressesFragment::class.java.simpleName, bundleOf(
                        ADDRESS_ID to it.id,
                        ADDRESS_NAME to it.fullAddress
                    )
                )
                findNavController().popBackStack()
            } else {
                navigate(AddressesFragmentDirections.toAddressDetailsFragment(it.id))
            }

        }
    }

    private fun initAddressOptionsBottomSheet() {
        addressAdapter.onClickMoreButton = { address, position ->
            val bottomSheet = AddressOptionsBottomSheet(address.isPrimary)

            bottomSheet.onClickDeleteButton = {
                deleteAddress(address, position)
            }

            bottomSheet.onClickChangeDefault = {
                changeDefault(address)
            }

            bottomSheet.show(
                requireActivity().supportFragmentManager,
                ""
            )
        }
    }


    private fun deleteAddress(address: AddressEntity, position: Int) {
        viewModel.deleteAddress(address.id) { deleted: Boolean ->
            handleDeleteAddress(deleted, position)
        }
    }

    private fun changeDefault(address: AddressEntity) {
        viewModel.changeDefault(address.id) { changed: Boolean ->
            handleChangeDefaultAddress(changed)
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
        showDialog(response.message)
    }

    private fun handleAddressesLoaded(addressEntities: List<AddressEntity>) {
        addressAdapter.clearList()
        addressAdapter.addList(addressEntities)
        checkEmptyAddresses()
    }

    private fun checkEmptyAddresses() {
        binding.recAddresses.setupEmptyState(addressAdapter.dataList.isEmpty())
    }


    private fun handleLoading(loading: Boolean) {
        showLoading(loading)
    }


    companion object {
        const val ADDRESS_ID = "address_id"
        const val ADDRESS_NAME = "address_name"
    }


    private fun goToAddAddressFragment() {
        navigate(AddressesFragmentDirections.toAddAddressFragment())
    }

    override fun onRefresh() {
        viewModel.getAddresses()
        binding.refreshSwiper.isRefreshing = false
    }
}