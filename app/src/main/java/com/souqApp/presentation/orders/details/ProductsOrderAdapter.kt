package com.souqApp.presentation.orders.details

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.databinding.ItemProductInOrderDetailsBinding
import com.souqApp.domain.orders.ProductInOrderEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter

class ProductsOrderAdapter :
    BaseRecyclerAdapter<ItemProductInOrderDetailsBinding, ProductInOrderEntity>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.productInOrder, getItemByPosition(position))

    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemProductInOrderDetailsBinding {
        return ItemProductInOrderDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {

    }
}