package com.souqApp.data.product_details.remote

import com.google.gson.annotations.SerializedName

data class RelevantProductResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("product_name") val productName: String,
    @SerializedName("image") val image: String,
)