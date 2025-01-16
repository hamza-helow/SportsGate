package com.souqApp.presentation.main.cart.checkout_details

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.R
import com.souqApp.data.common.utlis.Constants
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.databinding.FragmentPaymentDetailsBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CheckoutDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import com.souqApp.domain.main.cart.entity.PaymentMethodEntity
import com.souqApp.infra.extension.errorBorder
import com.souqApp.infra.extension.secondOrNull
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.successBorder
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
    private lateinit var paymentMethodsAdapter: PaymentMethodsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToLoading()
        observeToCheckoutDetails()
        observeTocCheckCouponCode()
        observeToPaymentMethods()
        initListener()
        observeToValidate()
        binding.radioButtonHomeDelivery.setOnCheckedChangeListener(this)
        binding.radioButtonSitePickup.setOnCheckedChangeListener(this)
    }

    private fun observeToPaymentMethods() {
        viewModel.paymentMethodsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResult.Errors -> Unit
                is BaseResult.Success -> onPaymentMethodsLoaded(it.data)
            }
        }
    }

    private fun onPaymentMethodsLoaded(methods: List<PaymentMethodEntity>) {

        if (viewModel.selectedPaymentMethod == null) {
            viewModel.selectedPaymentMethod = methods.firstOrNull()
        }

        paymentMethodsAdapter =
            PaymentMethodsAdapter({ viewModel.selectedPaymentMethod }, ::handleSelectPaymentMethod)
        paymentMethodsAdapter.replaceList(methods)
        binding.recPaymentMethods.setAdapter(
            paymentMethodsAdapter,
            LinearLayoutManager(requireContext())
        )
    }


    private fun handleSelectPaymentMethod(paymentMethodEntity: PaymentMethodEntity) {
        viewModel.selectedPaymentMethod = paymentMethodEntity
    }

    private fun observeTocCheckCouponCode() {
        viewModel.checkCouponCodeLiveData.observe(viewLifecycleOwner, ::handleCheckCouponCode)
    }

    private fun observeToCheckoutDetails() {
        viewModel.checkoutDetailsLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is BaseResult.Errors -> handleCheckoutDetailsErrorLoad(result.error)
                is BaseResult.Success -> handleCheckoutDetailsLoaded(result.data)
            }
        }
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun initListener() {
        binding.btnBuy.setOnClickListener(this)
        binding.txtSubmitPromoCode.setOnClickListener(this)
        binding.etAddress.setOnClickListener(this)
    }

    private fun observeToValidate() {
        viewModel.validateLiveData.observe(viewLifecycleOwner, binding.btnBuy::setEnabled)
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
        viewModel.checkout(couponCode = binding.promoCodeEdt.text.toString()) { result ->
            when (result) {
                is BaseResult.Errors -> handleCheckoutError(result.error)
                is BaseResult.Success -> handleCheckoutSuccess(result.data)
            }
        }
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