package com.souqApp.data.product_details.remote

import com.souqApp.data.main.home.remote.dto.ProductAdsEntity

data class ProductDetailsEntity(
    val id: Int,
    val name: String,
    val desc: String,
    val regular_price: Double,
    val on_sale: Boolean,
    val sale_price: Double,
    val discount: Double,
    val stock: Int,
    val user_favourite: Boolean,
    val percent_added_tax: Double,
    val setting_currency: String,
    val setting_percent_added_tax: Double,
    val images: List<ProductAdsEntity>,
    val relevant: List<RelevantProductResponse>
)