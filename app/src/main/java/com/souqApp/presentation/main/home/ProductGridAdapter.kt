package com.souqApp.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
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

        holder.bind(BR.product, getItemByPosition(position))
        holder.bind(BR.showPrice, firebaseConfig.getBoolean(IS_PURCHASE_ENABLED))
        holder.itemView.setOnClickListener {
            onClickItem(getItemByPosition(position).id)
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