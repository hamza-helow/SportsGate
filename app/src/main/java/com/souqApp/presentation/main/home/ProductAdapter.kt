package com.souqApp.presentation.main.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.main.home.remote.dto.ProductResponse
import com.souqApp.databinding.ItemProductBinding
import com.souqApp.domain.main.home.entity.ProductEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.infra.utils.PRODUCTS_TYPE
import com.souqApp.presentation.products_by_type.ProductsByTypeActivity

class ProductAdapter() : BaseRecyclerAdapter<ItemProductBinding, ProductEntity>() {

    companion object {
        const val RECOMMENDED_PRODUCTS = 2
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.product, getItemByPosition(position))

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