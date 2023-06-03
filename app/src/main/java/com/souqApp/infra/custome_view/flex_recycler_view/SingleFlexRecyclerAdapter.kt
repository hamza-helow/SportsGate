package com.souqApp.infra.custome_view.flex_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class SingleFlexRecyclerAdapter<VB : ViewDataBinding, MODEL> :
    FlexRecyclerAdapter<MODEL>() {

    final override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): FlexViewHolder {

        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )

        return Holder(method.invoke(null, inflater, parent, false) as VB)
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