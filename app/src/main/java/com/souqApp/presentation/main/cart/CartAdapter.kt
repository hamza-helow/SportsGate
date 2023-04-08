package com.souqApp.presentation.main.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.main.cart.remote.dto.ProductInCartResponse
import com.souqApp.databinding.ItemCartBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter

class CartAdapter : BaseRecyclerAdapter<ItemCartBinding, ProductInCartResponse>() {

    lateinit var onChangeQTY: ((ProductInCartResponse) -> Unit)

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(BR.product, getItemByPosition(position))

        holder.binding.btnIncrease.setOnClickListener {
            getItemByPosition(position).qty += 1
            holder.binding.invalidateAll()
            onChangeQTY(getItemByPosition(position))

        }

        holder.binding.btnDecrease.setOnClickListener {
            if (getItemByPosition(position).qty > 1) {
                getItemByPosition(position).qty -= 1
                holder.binding.invalidateAll()
                onChangeQTY(getItemByPosition(position))
            }
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