package com.souqApp.presentation.payment_details

import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.souqApp.R
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.payment_details.remote.dto.CheckoutDetailsResponse
import com.souqApp.databinding.FragmentPaymentDetailsBinding
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import com.souqApp.domain.payment_details.CheckoutDetailsEntity
import com.souqApp.infra.extension.errorBorder
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.successBorder
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentDetailsFragment :
    BaseFragment<FragmentPaymentDetailsBinding>(FragmentPaymentDetailsBinding::inflate),
    View.OnClickListener {

    private val viewModel: PaymentDetailsViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        initListener()
        observer()
    }

    private fun initListener() {
        binding.btnBuy.setOnClickListener(this)
        binding.txtSubmitPromoCode.setOnClickListener(this)
    }

    private fun observer() {
        viewModel.state.observe(this) { handleState(it) }
    }

    private fun handleState(state: PaymentDetailsActivityState) {
        when (state) {
            is PaymentDetailsActivityState.Loading -> handleLoading(state.loading)
            is PaymentDetailsActivityState.Error -> handleError(state.throwable)
            is PaymentDetailsActivityState.CheckoutDetailsLoaded ->
                handleCheckoutDetailsLoaded(state.checkoutDetailsEntity)
            is PaymentDetailsActivityState.CheckoutDetailsErrorLoad ->
                handleCheckoutDetailsErrorLoad(state.response)
            is PaymentDetailsActivityState.AddressesLoaded -> handleAddressesLoaded(state.addressEntities)
            is PaymentDetailsActivityState.CheckoutSuccess -> handleCheckoutSuccess(state.checkoutEntity)
            is PaymentDetailsActivityState.CheckoutError -> handleCheckoutError(state.response)
            is PaymentDetailsActivityState.CheckCouponCode -> handleCheckCouponCode(state.valid)
            is PaymentDetailsActivityState.AddressesErrorLoad -> handleAddressesErrorLoad(state.response)
        }
    }

    private fun handleAddressesErrorLoad(response: WrappedListResponse<AddressResponse>) {
        requireContext().showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleCheckCouponCode(valid: Boolean) {
        if (!valid) {
            requireContext().showToast(getString(R.string.coupon_not_valid))
            binding.cardPromoCode.errorBorder()
        } else {
            requireContext().showToast(getString(R.string.successfully_operation))
            binding.cardPromoCode.successBorder()
        }
    }

    private fun handleCheckoutError(response: WrappedResponse<CheckoutResponse>) {
        requireContext().showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleCheckoutSuccess(checkoutEntity: CheckoutEntity) {
        requireContext().showToast(getString(R.string.successfully_operation))
        navigate(
            PaymentDetailsFragmentDirections.toOrderDetailsFragment(checkoutEntity.orderId),
            popUpTo = R.id.homeFragment
        )
    }

    private fun handleAddressesLoaded(addressEntities: List<AddressEntity>) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            addressEntities
        )
        binding.spinnerAddresses.adapter = adapter
    }

    private fun handleCheckoutDetailsErrorLoad(response: WrappedResponse<CheckoutDetailsResponse>) {
        requireContext().showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleCheckoutDetailsLoaded(checkoutDetailsEntity: CheckoutDetailsEntity) {
        binding.details = checkoutDetailsEntity
    }

    private fun handleError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    private fun handleLoading(loading: Boolean) {
        binding.btnBuy.isEnabled = !loading
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.btnBuy.id -> checkout()
            binding.txtSubmitPromoCode.id -> checkPromoCode()
        }
    }

    private fun checkPromoCode() {
        viewModel.checkCouponCode(binding.promoCodeEdt.text.toString())
    }

    private fun checkout() {
        val addressId = (binding.spinnerAddresses.selectedItem as AddressEntity).id
        val code = binding.promoCodeEdt.text.toString()
        viewModel.checkout(CheckoutRequest("$addressId", code))
    }
}