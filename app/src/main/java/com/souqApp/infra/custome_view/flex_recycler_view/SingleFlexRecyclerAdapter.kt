package com.souqApp.infra.custome_view.flex_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

@Suppress("UNCHECKED_CAST")
abstract class SingleFlexRecyclerAdapter<VB : ViewDataBinding, MODEL>(  private val inflate: Inflate<VB>) :
    FlexRecyclerAdapter<MODEL>() {

    final override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): FlexViewHolder {
        return Holder(inflate.invoke(inflater,parent , false))
    }

    override fun setupViewHolder(holder: FlexViewHolder, position: Int, item: MODEL) {
        setupViewHolder(holder as SingleFlexRecyclerAdapter<VB, MODEL>.Holder, position,item)
    }

    abstract fun setupViewHolder(
        holder: Holder,
        position: Int,
        item: MODEL
    )

    inner class Holder(override val binding: VB) : FlexViewHolder(binding)
}