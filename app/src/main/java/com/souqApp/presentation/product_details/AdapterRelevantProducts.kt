package com.souqApp.presentation.product_details

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import com.souqApp.data.product_details.remote.RelevantProductResponse
import com.souqApp.databinding.ItemProductRelevantBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.infra.utils.ID_PRODUCT

class AdapterRelevantProducts :
    BaseRecyclerAdapter<ItemProductRelevantBinding, RelevantProductResponse>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.relevantProduct, getItemByPosition(position))

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductDetailsFragment::class.java)
            intent.putExtra(ID_PRODUCT, getItemByPosition(position).id)
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemProductRelevantBinding {
        return ItemProductRelevantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {

    }
}