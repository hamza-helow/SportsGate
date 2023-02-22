package com.souqApp.presentation.sub_categories

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.databinding.ItemSubCategoryBinding
import com.souqApp.domain.sub_categories.SubCategoryEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter

class SubCategoriesAdapter(val onClickItem: (SubCategoryEntity) -> Unit) :
    BaseRecyclerAdapter<ItemSubCategoryBinding, SubCategoryEntity>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.subCategory, getItemByPosition(position))

        holder.itemView.setOnClickListener {
            onClickItem(getItemByPosition(position))
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemSubCategoryBinding {
        return ItemSubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
    }
}