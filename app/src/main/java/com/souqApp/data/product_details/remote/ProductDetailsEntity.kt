package com.souqApp.data.product_details.remote

import com.google.gson.annotations.SerializedName

data class ProductDetailsEntity(
    val id: Int,
    val name: String,
    val desc: String,
    val price: String,
    val discount_price: String,
    val discount_percentage: Double,
    val on_sale: Boolean,
    val stock: Int,
    val percent_added_tax: Double,
    val setting_currency: String,
    val setting_percent_added_tax: Double,
    val media: List<String>,
    val relevant: List<RelevantProductResponse>,
    val variations: List<Variation>,
    val combination_options: List<CombinationOption>,
    val variation_compaination_id: Int?,
    val is_favorite: Boolean
)

data class CombinationOption(
    val description: String,
    val id: Int,
    val value: String,
    val variation_id: Int
)

data class Variation(
    val id: Int,
    val label: String,
    val options: List<VariationOption>,
    val type: VariationType,

    @Transient
    var selectedValue: String = ""
)

data class VariationOption(
    val description: String,
    val id: Int,
    val media: List<String>,
    val value: String,
    val variation_id: Int,
)

enum class VariationType(val code: Int) {
    @SerializedName("color")
    COLOR(0),

    @SerializedName("text")
    TEXT(1),

    @SerializedName("image")
    IMAGE(2)
}