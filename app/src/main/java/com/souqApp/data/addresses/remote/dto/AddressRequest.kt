package com.souqApp.data.addresses.remote.dto

import com.google.gson.annotations.SerializedName

data class AddressRequest(
    @SerializedName("building") val building: Int,
    @SerializedName("street") val street: String,
    @SerializedName("floor") val floor: Int,
    @SerializedName("notes") val notes: String,
    @SerializedName("area_id") val area_id: Int,
    @SerializedName("city_id") val city_id: Int,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
)