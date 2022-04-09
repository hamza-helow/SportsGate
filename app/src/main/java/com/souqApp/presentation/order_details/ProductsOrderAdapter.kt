package com.souqApp.presentation.order_details

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.order_details.remote.ProductInOrder
import com.souqApp.databinding.ItemProductInOrderDetailsBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.infra.utils.ID_PRODUCT
import com.souqApp.presentation.product_details.ProductDetailsFragment

class ProductsOrderAdapter :
    BaseRecyclerAdapter<ItemProductInOrderDetailsBinding, ProductInOrder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.productInOrder, getItemByPosition(position))

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context , ProductDetailsFragment::class.java)
            intent.putExtra(ID_PRODUCT, getItemByPosition(position).id)
            holder.itemView.context.startActivity(intent)
        }
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