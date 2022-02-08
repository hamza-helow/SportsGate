package com.souqApp.presentation.addresses.addresses

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.R
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.databinding.FragmentAddressesBinding
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.infra.extension.isVisible
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

        addressAdapter.onClickMoreButton = { address, position ->
            val bottomSheet = AddressOptionsBottomSheet(address.isPrimary)

            bottomSheet.onClickDeleteButton = {
                viewModel.deleteAddress(address.id, position)
            }

            bottomSheet.show(
                requireActivity().supportFragmentManager,
                ""
            )
        }

        initListener()
        observer()


    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner, { handleState(it) })

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
            is AddressesFragmentState.DeleteAddress -> handleDeleteAddress(
                state.deleted,
                state.position
            )
        }
    }

    private fun handleDeleteAddress(deleted: Boolean, position: Int) {
        if (deleted) {
            addressAdapter.removeItem(position)
        }
    }

    private fun handleAddressesErrorLoad(response: WrappedListResponse<AddressResponse>) {

    }

    private fun handleAddressesLoaded(addressEntities: List<AddressEntity>) {
        addressAdapter.list = addressEntities
    }

    private fun handleError(throwable: Throwable) {

        Log.e("ereRr", throwable.stackTraceToString())
    }

    private fun handleLoading(loading: Boolean) {
        binding.content.isVisible(!loading)
        binding.progressBar.isVisible(loading)
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