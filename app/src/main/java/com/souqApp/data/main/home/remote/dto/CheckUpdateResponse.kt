package com.souqApp.data.main.home.remote.dto

import com.google.gson.annotations.SerializedName

data class CheckUpdateResponse(
    @SerializedName("force_update")
    val forceUpdate: Boolean?,
    @SerializedName("latest_version")
    val latestVersion: String?,
    @SerializedName("new_version")
    val newVersion: Boolean?,
    @SerializedName("store_url")
    val storeUrl: String?
)