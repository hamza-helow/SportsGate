package com.souqApp.data.main.home.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("logo") val logo: String?,
)