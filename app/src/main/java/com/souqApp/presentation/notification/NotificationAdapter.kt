package com.souqApp.presentation.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.databinding.ItemNotificationBinding

class NotificationAdapter(private val onClickItem: (NotificationEntity) -> Unit) :
    PagingDataAdapter<NotificationEntity, NotificationAdapter.ViewHolder>(DiffCallback) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NotificationEntity?, onClickItem: (NotificationEntity) -> Unit) {

            if (item == null)
                return

            binding.setVariable(BR.notification, item)
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onClickItem(item)
            }
        }
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<NotificationEntity>() {

        override fun areItemsTheSame(
            oldItem: NotificationEntity,
            newItem: NotificationEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NotificationEntity,
            newItem: NotificationEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

}