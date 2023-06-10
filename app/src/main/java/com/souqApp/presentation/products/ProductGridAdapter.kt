package com.souqApp.presentation.products

import com.souqApp.BR
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ItemProductGridBinding
import com.souqApp.infra.custome_view.flex_recycler_view.SingleFlexRecyclerAdapter

class ProductGridAdapter(private val onClickItem: (Int) -> Unit) :
    SingleFlexRecyclerAdapter<ItemProductGridBinding, ProductEntity>(ItemProductGridBinding::inflate) {

    override fun setupViewHolder(
        holder: Holder,
        position: Int,
        item: ProductEntity
    ) {
        holder.bind(BR.product, item)
        holder.binding.root.setOnClickListener {
            onClickItem(item.id)
        }
    }
}