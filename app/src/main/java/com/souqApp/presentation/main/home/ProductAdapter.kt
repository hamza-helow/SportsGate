package com.souqApp.presentation.main.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ItemProductBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.infra.utils.ID_PRODUCT
import com.souqApp.presentation.product_details.HomeProductDetailsActivity
import com.souqApp.presentation.product_details.ProductDetailsFragment

class ProductAdapter : BaseRecyclerAdapter<ItemProductBinding, ProductEntity>() {


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.product, getItemByPosition(position))
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, HomeProductDetailsActivity::class.java)
            intent.putExtra(ID_PRODUCT, getItemByPosition(position).id)
            holder.itemView.context.startActivity(intent)

        }

    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemProductBinding {
        return ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
    }
}