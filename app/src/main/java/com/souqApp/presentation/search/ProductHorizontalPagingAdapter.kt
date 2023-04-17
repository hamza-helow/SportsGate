package com.souqApp.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.souqApp.BR
import com.souqApp.data.common.utlis.Constants
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ItemProductHorizontalBinding
import com.souqApp.infra.utils.BaseViewHolder

class ProductHorizontalPagingAdapter(val onClickItem: (Int) -> Unit) :
    PagingDataAdapter<ProductEntity, ProductHorizontalPagingAdapter.SearchViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(BR.productInSearch, getItem(position))

        holder.binding.root.setOnClickListener {
            onClickItem(getItem(position)?.id ?: Constants.UNDEFINED_ID)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemProductHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class SearchViewHolder(override val binding: ItemProductHorizontalBinding) :
        BaseViewHolder<ItemProductHorizontalBinding>(binding)


    private companion object DiffCallback : DiffUtil.ItemCallback<ProductEntity>() {

        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem == newItem
        }
    }
}