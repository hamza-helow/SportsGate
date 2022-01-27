package com.souqApp.data.products_by_type.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductsByTypeRequest(
    @SerializedName("type") val type: Int,
    @SerializedName("sub_category_id") val sub_category_id: Int,
    @SerializedName("page") val page: Int,
    )