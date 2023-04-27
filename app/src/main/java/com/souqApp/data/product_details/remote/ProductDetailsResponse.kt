package com.souqApp.data.product_details.remote

import com.google.gson.annotations.SerializedName
import com.souqApp.domain.main.home.TagEntity

data class ProductDetailsResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("desc")
    val desc: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("discount_price")
    val discountPrice: String?,
    @SerializedName("discount_percentage")
    val discountPercentage: Double?,
    @SerializedName("is_salesable")
    val isSalesable: Boolean?,
    @SerializedName("qty")
    val qty: Int?,
    @SerializedName("media")
    val media: List<String>,
    @SerializedName("relevant")
    val relevant: List<RelevantProductResponse>,
    @SerializedName("variations")
    val variations: List<Variation>,
    @SerializedName("combination_options")
    val combinationOptions: List<CombinationOption>,
    @SerializedName("variation_compaination_id")
    val variationCompainationId: Int?,
    @SerializedName("is_favorite")
    val isFavorite: Boolean? ,
    @SerializedName("tags")
    val tags:List<TagEntity>
)