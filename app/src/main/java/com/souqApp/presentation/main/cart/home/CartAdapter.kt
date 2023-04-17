package com.souqApp.presentation.main.cart.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.main.cart.remote.dto.ProductInCartResponse
import com.souqApp.databinding.ItemCartBinding
import com.souqApp.domain.main.cart.entity.ProductInCartEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter

class CartAdapter(private val onChangeQTY: ((ProductInCartEntity, isIncrease: Boolean) -> Unit)) :
    BaseRecyclerAdapter<ItemCartBinding, ProductInCartEntity>() {


    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(BR.product, getItemByPosition(position))

        holder.binding.btnIncrease.setOnClickListener {
            onChangeQTY(getItemByPosition(position), true)

        }

        holder.binding.btnDecrease.setOnClickListener {
            onChangeQTY(getItemByPosition(position), false)
        }

    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemCartBinding {
        return ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
    }
}