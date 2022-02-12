package com.souqApp.presentation.orders

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.databinding.ItemOrderBinding
import com.souqApp.domain.orders.OrderEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.infra.utils.ID_ORDER
import com.souqApp.presentation.order_details.OrderDetailsActivity

class OrdersAdapter : BaseRecyclerAdapter<ItemOrderBinding, OrderEntity>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.order, getItemByPosition(position))

        // go to Order details activity
        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, OrderDetailsActivity::class.java)
            intent.putExtra(ID_ORDER, getItemByPosition(position).id)
            holder.itemView.context.startActivity(intent)
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