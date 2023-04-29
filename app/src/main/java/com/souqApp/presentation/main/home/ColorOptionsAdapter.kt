package com.souqApp.presentation.main.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.data.product_details.remote.VariationOption
import com.souqApp.databinding.ItemHomeVariationColorsBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter

class ColorOptionsAdapter  : BaseRecyclerAdapter<ItemHomeVariationColorsBinding, VariationOption>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]
        holder.binding.txtColor.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(item.description))
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemHomeVariationColorsBinding {
        return ItemHomeVariationColorsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun enableAddItem(): Boolean  =false

    override fun needLoadMore(page: Int) {}
}