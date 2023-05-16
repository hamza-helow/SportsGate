package com.souqApp.presentation.product_details

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.databinding.ItemPromotionBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter


class ImagesProductAdapter(private val onClickItem: (image: String) -> Unit = {}) :
    BaseRecyclerAdapter<ItemPromotionBinding, String>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]
        holder.bind(BR.image, item)

        holder.binding.root.setOnClickListener {
            onClickItem(list[position])
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemPromotionBinding {
        return ItemPromotionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean = false
    override fun needLoadMore(page: Int) {}
}