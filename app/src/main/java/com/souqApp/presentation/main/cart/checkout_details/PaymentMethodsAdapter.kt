package com.souqApp.presentation.main.cart.checkout_details

import android.annotation.SuppressLint
import com.souqApp.BR
import com.souqApp.databinding.ItemPaymentMethodBinding
import com.souqApp.domain.main.cart.entity.PaymentMethodEntity
import com.souqApp.infra.custome_view.flex_recycler_view.SingleFlexRecyclerAdapter

class PaymentMethodsAdapter(
    private val selectedPaymentMethod: () -> PaymentMethodEntity?,
    private val onSelect: (PaymentMethodEntity) -> Unit
) :
    SingleFlexRecyclerAdapter<ItemPaymentMethodBinding, PaymentMethodEntity>(
        ItemPaymentMethodBinding::inflate
    ) {

    @SuppressLint("NotifyDataSetChanged")
    override fun setupViewHolder(holder: Holder, position: Int, item: PaymentMethodEntity) {
        holder.bind(BR.paymentMethod, item)
        holder.binding.radioButtonMethod.isChecked = selectedPaymentMethod() == item

        holder.binding.radioButtonMethod.setOnClickListener {
            notifyDataSetChanged()
            onSelect(item)
        }
    }
}