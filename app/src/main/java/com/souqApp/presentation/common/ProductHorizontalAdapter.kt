package com.souqApp.presentation.common

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ItemProductHorizontalBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.infra.utils.ID_PRODUCT
import com.souqApp.presentation.product_details.ProductDetailsActivity

class ProductHorizontalAdapter : BaseRecyclerAdapter<ItemProductHorizontalBinding, ProductEntity>() {

    lateinit var listenerNeedLoadMore: ((Int) -> Unit)

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.productInSearch, getItemByPosition(position))

        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductDetailsActivity::class.java)
            intent.putExtra(ID_PRODUCT, getItemByPosition(position).id)
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemProductHorizontalBinding {
        return ItemProductHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
        listenerNeedLoadMore(page)
    }
}