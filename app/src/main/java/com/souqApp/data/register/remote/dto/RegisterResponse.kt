package com.souqApp.data.register.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponse(@SerializedName("token") val token: String)