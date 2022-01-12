package com.souqApp.data.main.home.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductAdsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String,
)