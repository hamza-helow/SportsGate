package com.souqApp.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.main.home.remote.dto.ProductAdsEntity
import com.souqApp.databinding.ItemPromotionBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter

class PromotionAdapter(val onClickItem: (Int) -> Unit) :
    BaseRecyclerAdapter<ItemPromotionBinding, ProductAdsEntity>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]
        holder.bind(BR.image, item.image)
        holder.itemView.setOnClickListener {
            onClickItem(item.id)
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemPromotionBinding {
        return ItemPromotionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean = false
    override fun needLoadMore(page: Int) {}
}