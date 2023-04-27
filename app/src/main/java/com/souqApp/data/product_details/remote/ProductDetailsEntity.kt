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