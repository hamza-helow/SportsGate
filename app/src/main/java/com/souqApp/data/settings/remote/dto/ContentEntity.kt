package com.souqApp.data.settings.remote.dto

import com.google.gson.annotations.SerializedName

data class ContentEntity(
    @SerializedName("content")
    val content: String
)