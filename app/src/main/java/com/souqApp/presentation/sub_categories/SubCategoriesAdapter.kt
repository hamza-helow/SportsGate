package com.souqApp.presentation.sub_categories

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.databinding.ItemSubCategoryBinding
import com.souqApp.domain.sub_categories.SubCategoryEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.infra.utils.ID_SUBCATEGORY
import com.souqApp.infra.utils.PRODUCTS_TYPE
import com.souqApp.infra.utils.PRODUCT_BY_SUB_CATEGORY
import com.souqApp.presentation.products_by_type.ProductsByTypeActivity

class SubCategoriesAdapter : BaseRecyclerAdapter<ItemSubCategoryBinding, SubCategoryEntity>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.subCategory, getItemByPosition(position))

        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, ProductsByTypeActivity::class.java)
            intent.putExtra(PRODUCTS_TYPE, PRODUCT_BY_SUB_CATEGORY)
            intent.putExtra(ID_SUBCATEGORY, getItemByPosition(position).id)
            holder.itemView.context.startActivity(intent)
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