package com.souqApp.data.products.remote.dto

import com.google.gson.annotations.SerializedName

data class AddToFavoriteResponse(
    @SerializedName("user_favourite")
    val userFavourite: Boolean
)