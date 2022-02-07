package com.souqApp.data.addresses.remote.dto

import com.google.gson.annotations.SerializedName

class CityResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("areas") val areas: List<AreaResponse>
)