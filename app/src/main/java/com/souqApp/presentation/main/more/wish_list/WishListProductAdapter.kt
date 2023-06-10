package com.souqApp.presentation.main.more.wish_list

import com.souqApp.BR
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ItemProductHorizontalBinding
import com.souqApp.infra.custome_view.flex_recycler_view.SingleFlexRecyclerAdapter

class WishListProductAdapter (val onClickItem:(Int)->Unit): SingleFlexRecyclerAdapter<ItemProductHorizontalBinding, ProductEntity>() {


    override fun setupViewHolder(holder: Holder, position: Int, item: ProductEntity) {
        holder.bind(BR.product, item)
        holder.binding.root.setOnClickListener { onClickItem(it.id) }
    }
}