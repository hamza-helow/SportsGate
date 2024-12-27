package com.souqApp.data.main.cart.remote.dto

import com.google.gson.annotations.SerializedName

data class PaymentMethodResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("label")
    val label: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("description")
    val description: String?,
)