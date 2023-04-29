package com.souqApp.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ItemProductGridBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.infra.utils.IS_PURCHASE_ENABLED

class ProductGridAdapter(
    private val firebaseConfig: FirebaseRemoteConfig,
    val onClickItem: (Int) -> Unit
) :
    BaseRecyclerAdapter<ItemProductGridBinding, ProductEntity>() {

    lateinit var listenerNeedLoadMore: ((Int) -> Unit)

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val product = getItemByPosition(position)
        holder.bind(BR.product, product)
        holder.bind(BR.showPrice, firebaseConfig.getBoolean(IS_PURCHASE_ENABLED))
        holder.itemView.setOnClickListener {
            onClickItem(product.id)
        }

        product.variations.firstOrNull()?.let { variation ->
            val colorOptionsAdapter = ColorOptionsAdapter()
            colorOptionsAdapter.list = variation.options
            holder.binding.recColors.layoutManager =
                LinearLayoutManager(holder.itemView.context, RecyclerView.HORIZONTAL, false)
            holder.binding.recColors.adapter = colorOptionsAdapter
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemProductGridBinding {
        return ItemProductGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
        listenerNeedLoadMore(page)
    }
}