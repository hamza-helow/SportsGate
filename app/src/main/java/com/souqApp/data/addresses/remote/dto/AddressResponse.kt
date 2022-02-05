package com.souqApp.data.addresses.remote.dto

import com.google.gson.annotations.SerializedName

data class AddressResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("full_address") val fullAddress: String,
    @SerializedName("is_default") val isDefault: Boolean, )