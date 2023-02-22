package com.souqApp.presentation.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.databinding.ItemOrderBinding
import com.souqApp.domain.orders.OrderEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter

class OrdersAdapter(val onClickItem: (Int) -> Unit) :
    BaseRecyclerAdapter<ItemOrderBinding, OrderEntity>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.order, getItemByPosition(position))
        holder.binding.root.setOnClickListener {
            onClickItem(getItemByPosition(position).id)
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemOrderBinding {
        return ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {

    }
}