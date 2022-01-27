package com.souqApp.presentation.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import com.souqApp.databinding.ItemProductGridBinding
import com.souqApp.domain.main.home.entity.ProductEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter

class ProductGridAdapter : BaseRecyclerAdapter<ItemProductGridBinding, ProductEntity>() {

    lateinit var listenerNeedLoadMore: ((Int) -> Unit)

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.product, getItemByPosition(position))
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemProductGridBinding {
        return ItemProductGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
            listenerNeedLoadMore(page)
         Log.e("ERer" , "new page $page")
    }
}