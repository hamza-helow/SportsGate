package com.souqApp.domain.main.home.entity

data class ProductEntity(
    val id: Int?,
    val name: String?,
    val desc: String?,
    val smallDesc: String?,
    val firstImage: String?,
    val regularPrice: Double?,
    val onSale: Boolean?,
    val salePrice: Double?,
    val userFavourite: Boolean?,
    val percentAddedTax: Double?,
    val settingCurrency: String?,
    val settingPercentAddedTax: Double?,
)