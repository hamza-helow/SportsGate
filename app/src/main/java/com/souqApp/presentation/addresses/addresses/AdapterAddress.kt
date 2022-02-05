package com.souqApp.presentation.addresses.addresses

import android.view.LayoutInflater
import android.view.ViewGroup
import com.souqApp.BR
import com.souqApp.databinding.ItemAddressBinding
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.infra.utils.BaseRecyclerAdapter

class AdapterAddress : BaseRecyclerAdapter<ItemAddressBinding, AddressEntity>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.address, getItemByPosition(position))

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