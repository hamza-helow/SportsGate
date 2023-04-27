package com.souqApp.domain.products

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ProductsType : Parcelable {
    PROMO,
    TAG,
    CATEGORY
}
