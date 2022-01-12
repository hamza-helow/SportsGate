package com.souqApp.data.main.home.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("small_desc") val smallDesc: String?,
    @SerializedName("first_image") val firstImage: String?,
    @SerializedName("regularPrice") val regularPrice: Double?,
    @SerializedName("on_sale") val onSale: Boolean?,
    @SerializedName("sale_price") val salePrice: Double?,
    @SerializedName("user_favourite") val userFavourite: Boolean?,
    @SerializedName("percent_added_tax") val percentAddedTax: Double?,
    @SerializedName("setting_currency") val settingCurrency: String?,
    @SerializedName("setting_percent_added_tax") val settingPercentAddedTax: Double?,
)