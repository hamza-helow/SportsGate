package com.souqApp.data.product_details.remote

import com.google.gson.annotations.SerializedName

data class AddToFavoriteResponse(
    @SerializedName("user_favourite")
    val userFavourite: Boolean
)