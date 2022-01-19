package com.souqApp.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.main.home.remote.dto.ProductResponse
import com.souqApp.databinding.ItemProductBinding
import com.souqApp.domain.main.home.entity.ProductEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter

class ProductAdapter() : BaseRecyclerAdapter<ItemProductBinding, ProductEntity>() {

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