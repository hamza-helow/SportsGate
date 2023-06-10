package com.souqApp.presentation.addresses.addresses

import com.souqApp.BR
import com.souqApp.databinding.ItemAddressBinding
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.infra.custome_view.flex_recycler_view.SingleFlexRecyclerAdapter

class AdapterAddress : SingleFlexRecyclerAdapter<ItemAddressBinding, AddressEntity>() {

    lateinit var onClickMoreButton: ((AddressEntity, Int) -> Unit)

    lateinit var onClickItem: ((AddressEntity) -> Unit)

    override fun setupViewHolder(holder: Holder, position: Int, item: AddressEntity) {
        holder.bind(BR.address, item)

        holder.binding.imgMore.setOnClickListener {
            onClickMoreButton(item, position)
        }

        holder.binding.root.setOnClickListener {
            onClickItem(item)
        }
    }
}