package com.souqApp.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.BR
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ItemProductBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.infra.utils.IS_PURCHASE_ENABLED

class ProductAdapter(
    private val firebaseConfig: FirebaseRemoteConfig,
    val onClickItem: (Int) -> Unit

) : BaseRecyclerAdapter<ItemProductBinding, ProductEntity>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val product = getItemByPosition(position)
        holder.bind(BR.product, product)
        holder.bind(BR.showPrice, firebaseConfig.getBoolean(IS_PURCHASE_ENABLED))



        product.variations.firstOrNull()?.let { variation ->
            val colorOptionsAdapter = ColorOptionsAdapter()
            colorOptionsAdapter.list = variation.options
            holder.binding.recColors.layoutManager =
                LinearLayoutManager(holder.itemView.context, RecyclerView.HORIZONTAL, false)
            holder.binding.recColors.adapter = colorOptionsAdapter
        }

        holder.itemView.setOnClickListener {
            onClickItem(product.id)
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemProductBinding {
        return ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
    }
}