package com.souqApp.domain.product_details

import com.souqApp.data.main.home.remote.dto.ProductAdsResponse
import com.souqApp.data.product_details.remote.RelevantProductResponse

data class ProductDetailsEntity(
    val id: Int,
    val name: String,
    val desc: String,
    val regularPrice: Double,
    val onSale: Boolean,
    val salePrice: Double,
    val discount: Double,
    val stock: Int,
    val userFavourite: Boolean,
    val percentAddedTax: Double,
    val settingCurrency: String,
    val settingPercentAddedTax: Double,
    val images: List<ProductAdsResponse>,
    val relevant: List<RelevantProductResponse>
)