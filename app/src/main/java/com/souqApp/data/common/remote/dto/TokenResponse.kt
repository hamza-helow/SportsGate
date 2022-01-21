package com.souqApp.data.common.remote.dto

import com.google.gson.annotations.SerializedName

data class TokenResponse(@SerializedName("token") val token: String)