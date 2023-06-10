package com.souqApp.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ItemRecommendedProductBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter

class ProductRecommendedAdapter(
    val onClickItem: (Int) -> Unit

) : BaseRecyclerAdapter<ItemRecommendedProductBinding, ProductEntity>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val product = getItemByPosition(position)
        holder.bind(BR.product, product)

        holder.itemView.setOnClickListener {
            onClickItem(product.id)
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemRecommendedProductBinding {
        return ItemRecommendedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
    }
}