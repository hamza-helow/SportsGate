package com.souqApp.presentation.main.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ItemProductGridBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.infra.utils.ID_PRODUCT
import com.souqApp.infra.utils.IS_PURCHASE_ENABLED
import com.souqApp.presentation.product_details.HomeProductDetailsActivity
import javax.inject.Inject

class ProductGridAdapter @Inject constructor(private val firebaseConfig: FirebaseRemoteConfig) :
    BaseRecyclerAdapter<ItemProductGridBinding, ProductEntity>() {

    lateinit var listenerNeedLoadMore: ((Int) -> Unit)

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(BR.product, getItemByPosition(position))
        holder.bind(BR.showPrice , firebaseConfig.getBoolean(IS_PURCHASE_ENABLED))
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, HomeProductDetailsActivity::class.java)
            intent.putExtra(ID_PRODUCT, getItemByPosition(position).id)
            holder.itemView.context.startActivity(intent)
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