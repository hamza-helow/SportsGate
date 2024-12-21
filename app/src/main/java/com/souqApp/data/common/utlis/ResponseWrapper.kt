package com.souqApp.data.common.utlis

import com.google.gson.annotations.SerializedName

data class WrappedListResponse<T>(
    var code: Int,
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: Boolean,
    @SerializedName("errors") var errors: List<String>? = null,
    @SerializedName("data") var data: List<T>? = null,
    @SerializedName("current_page") val currentPage: Int? = null,
    @SerializedName("per_page") val totalPages: Int? = null

)

data class WrappedResponse<T>(
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: Boolean,
    @SerializedName("errors") var errors: List<String>? = null,
    @SerializedName("data") var data: T

) {

    fun formattedErrors(): String {
        return formattedErrors(this.errors)
    }
}

fun formattedErrors(errors: List<String>?): String {
    return errors.orEmpty().joinToString { "$it\n" }
}
