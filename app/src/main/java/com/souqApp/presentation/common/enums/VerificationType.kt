package com.souqApp.presentation.common.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class VerificationType : Parcelable {
    BY_EMAIL,
    BY_PHONE
}