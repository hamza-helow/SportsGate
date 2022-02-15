package com.souqApp.data.main.home.remote.dto

data class ProductEntity(
    val desc: String,
    val discount: Double,
    val first_image: String,
    val id: Int,
    val name: String,
    val on_sale: Boolean,
    val percent_added_tax: Double,
    val regular_price: Double,
    val sale_price: Double,
    val setting_currency: String,
    val setting_percent_added_tax: Double,
    val small_desc: String,
    val user_favourite: Boolean
)