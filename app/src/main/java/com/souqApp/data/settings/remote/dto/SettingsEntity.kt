package com.souqApp.data.settings.remote.dto

import com.google.gson.annotations.SerializedName

data class SettingsEntity(
    @SerializedName("social_links")
    val socialLinks: SocialLinksEntity?,
    @SerializedName("store_name")
    val storeName: String?
)