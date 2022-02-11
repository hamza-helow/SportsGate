package com.souqApp.data.addresses.remote.dto

import com.google.gson.annotations.SerializedName

data class AddressRequest(
    @SerializedName("address_id") val address_id: Int? = null,
    @SerializedName("building") val building: String,
    @SerializedName("street") val street: String,
    @SerializedName("floor") val floor: String,
    @SerializedName("notes") val notes: String,
    @SerializedName("area_id") val area_id: Int,
    @SerializedName("city_id") val city_id: Int,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
)