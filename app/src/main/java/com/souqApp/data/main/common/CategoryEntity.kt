package com.souqApp.data.main.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryEntity(
    val id: Int,
    val name: String?,
    val children: List<CategoryEntity>?
) :Parcelable