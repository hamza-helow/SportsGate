package com.souqApp.data.product_details.remote

import com.google.gson.annotations.SerializedName

data class ImageProductResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String,
)