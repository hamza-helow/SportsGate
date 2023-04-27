package com.souqApp.presentation.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.BR
import com.souqApp.data.common.utlis.Constants
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ItemProductGridBinding
import com.souqApp.infra.utils.BaseViewHolder
import com.souqApp.infra.utils.IS_PURCHASE_ENABLED

class ProductGridPagingAdapter(
    private val firebaseConfig: FirebaseRemoteConfig,
    private val onClickItem: (Int) -> Unit,
) :
    PagingDataAdapter<ProductEntity, ProductGridPagingAdapter.SearchViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(BR.product, getItem(position))
        holder.bind(BR.showPrice, firebaseConfig.getBoolean(IS_PURCHASE_ENABLED))

        holder.binding.root.setOnClickListener {
            onClickItem(getItem(position)?.id ?: Constants.UNDEFINED_ID)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemProductGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class SearchViewHolder(override val binding: ItemProductGridBinding) :
        BaseViewHolder<ItemProductGridBinding>(binding)


    private companion object DiffCallback : DiffUtil.ItemCallback<ProductEntity>() {

        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem == newItem
        }
    }
}