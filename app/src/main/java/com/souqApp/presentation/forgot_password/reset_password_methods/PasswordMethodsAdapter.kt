package com.souqApp.presentation.forgot_password.reset_password_methods

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.databinding.ItemPasswordMethodBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter

class PasswordMethodsAdapter(private val onClickItem: (String) -> Unit) :
    BaseRecyclerAdapter<ItemPasswordMethodBinding, String>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.root.setOnClickListener {
            onClickItem(list[position])
        }
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemPasswordMethodBinding {
        return ItemPasswordMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean = false

    override fun needLoadMore(page: Int) = Unit
}