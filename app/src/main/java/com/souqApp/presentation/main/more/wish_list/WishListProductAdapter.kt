package com.souqApp.presentation.main.more.wish_list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ItemProductHorizontalBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter

class WishListProductAdapter (val onClickItem:(Int)->Unit): BaseRecyclerAdapter<ItemProductHorizontalBinding, ProductEntity>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.product, getItemByPosition(position))

        holder.binding.root.setOnClickListener {
            onClickItem(getItemByPosition(position).id)
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemProductHorizontalBinding {
        return ItemProductHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
    }
}