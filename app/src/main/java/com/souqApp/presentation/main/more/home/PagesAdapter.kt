package com.souqApp.presentation.main.more.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.databinding.ItemPageBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter

class PagesAdapter(private val onClickPage: (PageEntity) -> Unit) :
    BaseRecyclerAdapter<ItemPageBinding, PageEntity>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val page = list[position]
        holder.bind(BR.page, page)

        holder.binding.root.setOnClickListener {
            onClickPage(page)
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemPageBinding {
        return ItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean = false

    override fun needLoadMore(page: Int) = Unit
}