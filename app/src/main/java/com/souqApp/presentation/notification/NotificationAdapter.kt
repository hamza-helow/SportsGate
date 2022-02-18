package com.souqApp.presentation.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.databinding.ItemNotificationBinding
import com.souqApp.infra.utils.BaseRecyclerAdapter

class NotificationAdapter : BaseRecyclerAdapter<ItemNotificationBinding, NotificationEntity>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BR.notification, getItemByPosition(position))
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemNotificationBinding {
        return ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
    }
}