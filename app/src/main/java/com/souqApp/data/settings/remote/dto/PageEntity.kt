package com.souqApp.data.settings.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PageEntity(
    val id: Int?,
    val title: String?,
) : Parcelable