package com.souqApp.presentation.addresses.addresses

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.databinding.ItemAddressBinding
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter

class AdapterAddress : BaseRecyclerAdapter<ItemAddressBinding, AddressEntity>() {

    lateinit var onClickMoreButton: ((AddressEntity, Int) -> Unit)

    lateinit var onClickItem: ((Int) -> Unit)

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.address, getItemByPosition(position))

        holder.binding.imgMore.setOnClickListener {
            onClickMoreButton(getItemByPosition(position), position)
        }

        holder.binding.root.setOnClickListener {
            onClickItem(getItemByPosition(position).id)
        }


    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemAddressBinding {
        return ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {

    }
}