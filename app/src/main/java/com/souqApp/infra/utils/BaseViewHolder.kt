package com.souqApp.infra.utils

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T : ViewDataBinding>(open val binding: T) : RecyclerView.ViewHolder(binding.root) {

    fun bind(idVar: Int, item: Any?) {
        binding.setVariable(idVar, item)
        binding.executePendingBindings()
    }
}