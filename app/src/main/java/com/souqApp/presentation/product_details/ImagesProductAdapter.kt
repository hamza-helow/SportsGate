package com.souqApp.presentation.product_details

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.databinding.ItemProductImageBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter


class ImagesProductAdapter(private val onClickItem: (image: String) -> Unit = {}) :
    BaseRecyclerAdapter<ItemProductImageBinding, String>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]
        holder.bind(BR.image, item)

        holder.binding.root.setOnClickListener {
            onClickItem(list[position])
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemProductImageBinding {
        return ItemProductImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean = false
    override fun needLoadMore(page: Int) {}
}