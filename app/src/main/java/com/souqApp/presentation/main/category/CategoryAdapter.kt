package com.souqApp.presentation.main.category

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.databinding.ItemCategoryBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter

class CategoryAdapter(
    val onClickItem: (CategoryEntity) -> Unit
) : BaseRecyclerAdapter<ItemCategoryBinding, CategoryEntity>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.category, getItemByPosition(position))
        holder.binding.root.setOnClickListener {
            onClickItem(getItemByPosition(position))
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemCategoryBinding {
        return ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
    }

}