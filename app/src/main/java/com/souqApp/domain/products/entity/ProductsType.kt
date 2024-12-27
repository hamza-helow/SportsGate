package com.souqApp.domain.products.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ProductsType : Parcelable {
    PROMO,
    TAG,
    CATEGORY
}
