package com.souqApp.presentation.main.cart.home

import com.souqApp.BR
import com.souqApp.databinding.ItemCartBinding
import com.souqApp.domain.main.cart.entity.ProductInCartEntity
import com.souqApp.infra.custome_view.flex_recycler_view.SingleFlexRecyclerAdapter

class CartAdapter(private val onChangeQTY: ((ProductInCartEntity, isIncrease: Boolean) -> Unit)) :
    SingleFlexRecyclerAdapter<ItemCartBinding, ProductInCartEntity>(ItemCartBinding::inflate) {


    override fun setupViewHolder(holder: Holder, position: Int, item: ProductInCartEntity) {

        holder.bind(BR.product, item)

        holder.binding.btnIncrease.setOnClickListener {
            onChangeQTY(item, true)

        }

        holder.binding.btnDecrease.setOnClickListener {
            onChangeQTY(item, false)
        }
    }
}