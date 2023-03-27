package com.souqApp.data.main.home.remote.dto

data class ProductEntity(
    val desc: String,
    val discount: Double,
    val thumb: String,
    val id: Int,
    val name: String,
    val on_sale: Boolean,
    val percent_added_tax: Double,
    val price: String,
    val discounted_product_price:String,
    val discount_percentage:Double ,
    val sale_price: Double,
    val setting_currency: String,
    val setting_percent_added_tax: Double,
    val user_favourite: Boolean
)