package com.souqApp.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.databinding.ItemProductBinding
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter

class ProductAdapter : BaseRecyclerAdapter<ItemProductBinding, UserEntity>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {

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