package com.souqApp.presentation.payment_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.souqApp.R
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.payment_details.remote.dto.CheckoutDetailsResponse
import com.souqApp.databinding.ActivityPaymentDetailsBinding
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import com.souqApp.domain.payment_details.CheckoutDetailsEntity
import com.souqApp.infra.extension.*
import com.souqApp.infra.utils.ID_ORDER
import com.souqApp.presentation.main.MainActivity
import com.souqApp.presentation.order_details.OrderDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPaymentDetailsBinding

    private val viewModel: PaymentDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup(
            showTitleEnabled = true,
        )
        supportActionBar?.title = getString(R.string.payment_details)

        initListener()

        observer()
    }

    private fun initListener() {
        binding.btnBuy.setOnClickListener(this)
        binding.txtSubmitPromoCode.setOnClickListener(this)
    }

    private fun observer() {
        viewModel.state.observe(this, { handleState(it) })
    }

    private fun handleState(state: PaymentDetailsActivityState?) {
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
        showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleCheckCouponCode(valid: Boolean) {
        if (!valid) {
            showToast(getString(R.string.coupon_not_valid))
            binding.cardPromoCode.errorBorder()
        } else {
            showToast(getString(R.string.successfully_operation))
            binding.cardPromoCode.successBorder()
        }
    }

    private fun handleCheckoutError(response: WrappedResponse<CheckoutResponse>) {
        showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleCheckoutSuccess(checkoutEntity: CheckoutEntity) {
        showToast(getString(R.string.successfully_operation))

        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainIntent)

        val orderDetailsIntent = Intent(this, OrderDetailsActivity::class.java)
        orderDetailsIntent.putExtra(ID_ORDER, checkoutEntity.orderId)
        startActivity(orderDetailsIntent)
    }

    private fun handleAddressesLoaded(addressEntities: List<AddressEntity>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            addressEntities
        )
        binding.spinnerAddresses.adapter = adapter
    }

    private fun handleCheckoutDetailsErrorLoad(response: WrappedResponse<CheckoutDetailsResponse>) {
        showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleCheckoutDetailsLoaded(checkoutDetailsEntity: CheckoutDetailsEntity) {
        binding.details = checkoutDetailsEntity
    }

    private fun handleError(throwable: Throwable) {
        Log.e("ERer", throwable.stackTraceToString())
    }

    private fun handleLoading(loading: Boolean) {
        binding.btnBuy.isEnabled = !loading

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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