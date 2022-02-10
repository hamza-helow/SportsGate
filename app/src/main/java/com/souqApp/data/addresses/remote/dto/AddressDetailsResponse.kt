package com.souqApp.data.addresses.remote.dto

import com.google.gson.annotations.SerializedName

data class AddressDetailsResponse(
    @SerializedName("area_id")
    val areaId: Int,
    @SerializedName("area_name")
    val areaName: String,
    @SerializedName("building")
    val building: String,
    @SerializedName("city_id")
    val cityId: Int,
    @SerializedName("city_name")
    val cityName: String,
    @SerializedName("floor")
    val floor: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("notes")
    val notes: String,
    @SerializedName("street")
    val street: String
)