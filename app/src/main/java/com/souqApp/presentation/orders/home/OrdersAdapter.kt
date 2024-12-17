package com.souqApp.presentation.orders.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.souqApp.BR
import com.souqApp.databinding.ItemOrderBinding
import com.souqApp.domain.orders.entity.OrderEntity

class OrdersAdapter(val onClickItem: (Int) -> Unit) :
    PagingDataAdapter<OrderEntity, OrdersAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    class ViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderEntity?, onClickItem: (Int) -> Unit) {

            if (item == null)
                return

            binding.setVariable(BR.order, item)
            binding.executePendingBindings()
            binding.root.setOnClickListener { onClickItem(item.id) }
        }
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<OrderEntity>() {

        override fun areItemsTheSame(
            oldItem: OrderEntity,
            newItem: OrderEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: OrderEntity,
            newItem: OrderEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

}