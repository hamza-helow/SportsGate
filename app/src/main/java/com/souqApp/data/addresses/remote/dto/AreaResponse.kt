package com.souqApp.data.addresses.remote.dto

import com.google.gson.annotations.SerializedName

data class AreaResponse(
    @SerializedName("id") val id: Int ,
    @SerializedName("name") val name: String ,
)