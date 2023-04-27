package com.souqApp.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.databinding.ItemTagBinding
import com.souqApp.domain.main.home.TagEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter

class TagAdapter(private val onClick: (TagEntity) -> Unit) :
    BaseRecyclerAdapter<ItemTagBinding, TagEntity>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]
        holder.bind(BR.value, list[position].name)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemTagBinding {
        return ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean = false

    override fun needLoadMore(page: Int) {
    }
}