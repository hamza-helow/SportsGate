package com.souqApp.data.sub_categories.remote.dto

import com.google.gson.annotations.SerializedName

data class SubCategoryResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("logo") val logo: String?,
)