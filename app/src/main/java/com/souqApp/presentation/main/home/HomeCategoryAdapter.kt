package com.souqApp.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.databinding.ItemHomeCategoryBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter


class HomeCategoryAdapter(
    private val verticalMode: Boolean = true,
    val onClickItem: (CategoryEntity) -> Unit
) :
    BaseRecyclerAdapter<ItemHomeCategoryBinding, CategoryEntity>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.category, getItemByPosition(position))
        holder.bind(BR.verticalMode, verticalMode)
        holder.binding.root.setOnClickListener {
            onClickItem(getItemByPosition(position))
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemHomeCategoryBinding {
        return ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
    }
}