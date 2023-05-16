package com.souqApp.data.product_details.remote

import com.google.gson.annotations.SerializedName
import com.souqApp.domain.main.home.TagEntity

data class ProductDetailsEntity(
    val id: Int,
    val name: String,
    val desc: String,
    val price: String,
    val discountPrice: String,
    val discountPercentage: Double,
    val onSale: Boolean,
    val qty: Int,
    val media: List<String>,
    val relevant: List<RelevantProductResponse>,
    val variations: List<Variation>,
    val combinationOptions: List<CombinationOption>,
    val variationCompainationId: Int?,
    val isFavorite: Boolean ,
    val tags: List<TagEntity>
)

data class CombinationOption(
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("value")
    val value: String,
    @SerializedName("variation_id")
    val variationId: Int
)

data class Variation(
    @SerializedName("id")
    val id: Int,
    @SerializedName("label")
    val label: String,
    @SerializedName("options")
    val options: List<VariationOption>,
    @SerializedName("type")
    val type: VariationType,

    @Transient
    var selectedValue: String = ""
)

data class VariationOption(
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("media")
    val media: List<String>,
    @SerializedName("value")
    val value: String,
    @SerializedName("variation_id")
    val variationId: Int,
)

enum class VariationType(val code: Int) {
    @SerializedName("color")
    COLOR(0),

    @SerializedName("text")
    TEXT(1),

    @SerializedName("image")
    IMAGE(2)
}