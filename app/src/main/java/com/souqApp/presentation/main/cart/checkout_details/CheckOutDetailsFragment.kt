package com.souqApp.presentation.main.cart.checkout_details

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.souqApp.R
import com.souqApp.data.common.utlis.Constants
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.databinding.FragmentPaymentDetailsBinding
import com.souqApp.domain.main.cart.entity.CheckoutDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import com.souqApp.infra.extension.errorBorder
import com.souqApp.infra.extension.secondOrNull
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.successBorder
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.presentation.activity.MainViewModel
import com.souqApp.presentation.addresses.addresses.AddressesFragment
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckOutDetailsFragment :
    BaseFragment<FragmentPaymentDetailsBinding>(FragmentPaymentDetailsBinding::inflate),
    View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private val viewModel: PaymentDetailsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        observer()
        binding.radioButtonHomeDelivery.setOnCheckedChangeListener(this)
        binding.radioButtonSitePickup.setOnCheckedChangeListener(this)
    }

    private fun initListener() {
        binding.btnBuy.setOnClickListener(this)
        binding.txtSubmitPromoCode.setOnClickListener(this)
        binding.etAddress.setOnClickListener(this)
    }

    private fun observer() {
        viewModel.validateLiveData.observe(viewLifecycleOwner, binding.btnBuy::setEnabled)
        viewModel.state.observe(viewLifecycleOwner) { handleState(it) }
    }

    private fun handleState(state: PaymentDetailsFragmentState) {
        when (state) {
            is PaymentDetailsFragmentState.Loading -> handleLoading(state.loading)
            is PaymentDetailsFragmentState.Error -> handleError(state.throwable)
            is PaymentDetailsFragmentState.CheckoutDetailsLoaded -> handleCheckoutDetailsLoaded(
                state.checkoutDetailsEntity
            )

            is PaymentDetailsFragmentState.CheckoutDetailsErrorLoad -> handleCheckoutDetailsErrorLoad(
                state.response
            )

            is PaymentDetailsFragmentState.CheckoutSuccess -> handleCheckoutSuccess(state.checkoutEntity)
            is PaymentDetailsFragmentState.CheckoutError -> handleCheckoutError(state.response)
            is PaymentDetailsFragmentState.CheckCouponCode -> handleCheckCouponCode(state.valid)
        }
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
        showDialog(response.message)
    }

    private fun handleCheckoutSuccess(checkoutEntity: CheckoutEntity) {
        mainViewModel.setQty(0)
        navigate(CheckOutDetailsFragmentDirections.toCheckoutCompleted(checkoutEntity.orderId))
    }

    private fun handleCheckoutDetailsErrorLoad(response: WrappedResponse<CheckoutDetailsResponse>) {
        showDialog(response.message)
    }

    private fun handleCheckoutDetailsLoaded(checkoutDetailsEntity: CheckoutDetailsEntity) {
        binding.details = checkoutDetailsEntity
        viewModel.defaultIdAddress = checkoutDetailsEntity.userAddress?.id
        viewModel.selectedDeliveryOptionId = checkoutDetailsEntity.deliveryOptionId
        binding.deliveryOptionOne = checkoutDetailsEntity.deliveryOptions.firstOrNull()
        binding.deliveryOptionTwo = checkoutDetailsEntity.deliveryOptions.secondOrNull()
        viewModel.validate()
    }

    private fun handleError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    private fun handleLoading(loading: Boolean) {
        showLoading(loading)
        binding.btnBuy.isEnabled = !loading
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.btnBuy.id -> checkout()
            binding.txtSubmitPromoCode.id -> checkPromoCode()
            binding.etAddress.id -> navigateToAddressesFragment()
        }
    }

    private fun navigateToAddressesFragment() {
        navigate(CheckOutDetailsFragmentDirections.toAddressesGraph().apply {
            this.selectedMode = true
        })

        observeToAddressesFragmentResult()
    }

    private fun observeToAddressesFragmentResult() {
        setFragmentResultListener(AddressesFragment::class.java.simpleName) { _, bundle ->
            val id = bundle.getInt(AddressesFragment.ADDRESS_ID, Constants.UNDEFINED_ID)
            val name = bundle.getString(AddressesFragment.ADDRESS_NAME).orEmpty()
            binding.selectedFullAddress = name
            viewModel.selectedIdAddress = id
            viewModel.validate()
        }
    }

    private fun checkPromoCode() {
        if (binding.promoCodeEdt.text.toString().isNotBlank())
            viewModel.checkCouponCode(binding.promoCodeEdt.text.toString())
    }

    private fun checkout() {
        viewModel.checkout(couponCode = binding.promoCodeEdt.text.toString())
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            if (buttonView.id == binding.radioButtonHomeDelivery.id) {
                binding.radioButtonSitePickup.isChecked = false
                binding.etAddress.isEnabled = true
                binding.deliveryOptionOne?.let { viewModel.getCheckoutDetails(it.id) }
            } else {
                binding.radioButtonHomeDelivery.isChecked = false
                binding.etAddress.isEnabled = false
                binding.deliveryOptionTwo?.let { viewModel.getCheckoutDetails(it.id) }
            }
        }
        viewModel.validate()
    }
}