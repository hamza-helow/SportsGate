package com.souqApp.presentation.main.cart.checkout_details

import android.annotation.SuppressLint
import com.souqApp.BR
import com.souqApp.databinding.ItemPaymentMethodBinding
import com.souqApp.domain.main.cart.entity.PaymentMethodEntity
import com.souqApp.infra.custome_view.flex_recycler_view.SingleFlexRecyclerAdapter

class PaymentMethodsAdapter :
    SingleFlexRecyclerAdapter<ItemPaymentMethodBinding, PaymentMethodEntity>(
        ItemPaymentMethodBinding::inflate
    ) {

    private var selectedItemPosition = 0

    @SuppressLint("NotifyDataSetChanged")
    override fun setupViewHolder(holder: Holder, position: Int, item: PaymentMethodEntity) {
        holder.bind(BR.paymentMethod, item)
        holder.binding.radioButtonMethod.isChecked = position == selectedItemPosition

        holder.binding.radioButtonMethod.setOnClickListener {
            selectedItemPosition = position
            notifyDataSetChanged()
        }
    }
}