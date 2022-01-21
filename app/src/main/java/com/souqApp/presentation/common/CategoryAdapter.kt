package com.souqApp.presentation.common

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import com.souqApp.databinding.ItemCategoryBinding
import com.souqApp.domain.main.home.entity.CategoryEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.presentation.sub_categories.SubCategoriesActivity

/**
 * @param verticalMode  when use  LinearLayoutManager HORIZONTAL set verticalMode true else false
 * */
class CategoryAdapter(private val verticalMode: Boolean = true) :
    BaseRecyclerAdapter<ItemCategoryBinding, CategoryEntity>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.category, getItemByPosition(position))
        holder.bind(BR.verticalMode, verticalMode)

        holder.binding.root.setOnClickListener {
            goToSubCategoryActivity(holder.itemView.context, getItemByPosition(position))
        }
    }

    private fun goToSubCategoryActivity(context: Context, category: CategoryEntity) {
        val intent = Intent(context, SubCategoriesActivity::class.java)
        intent.putExtra(CategoryEntity::class.java.simpleName, category)
        context.startActivity(intent)
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