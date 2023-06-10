package com.souqApp.presentation.notification

import androidx.databinding.library.baseAdapters.BR
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.databinding.ItemNotificationBinding
import com.souqApp.infra.custome_view.flex_recycler_view.SingleFlexRecyclerAdapter

class NotificationAdapter : SingleFlexRecyclerAdapter<ItemNotificationBinding, NotificationEntity>() {
    override fun setupViewHolder(holder: Holder, position: Int, item: NotificationEntity) {
        holder.bind(BR.notification, item)
    }
}
