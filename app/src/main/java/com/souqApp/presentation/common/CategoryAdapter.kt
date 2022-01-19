package com.souqApp.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import com.souqApp.data.main.home.remote.dto.CategoryResponse
import com.souqApp.databinding.ItemCategoryBinding
import com.souqApp.domain.main.home.entity.CategoryEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter

/**
 * @param verticalMode  when use  LinearLayoutManager HORIZONTAL set verticalMode true else false
 * */
class CategoryAdapter(private val verticalMode: Boolean = true) :
    BaseRecyclerAdapter<ItemCategoryBinding, CategoryEntity>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.category, getItemByPosition(position))
        holder.bind(BR.verticalMode, verticalMode)
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