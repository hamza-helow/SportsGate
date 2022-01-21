package com.souqApp.data.common.mapper

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.main.common.CategoryResponse
import com.souqApp.data.main.home.remote.dto.ProductResponse
import com.souqApp.data.sub_categories.remote.dto.SubCategoryResponse
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.main.home.entity.CategoryEntity
import com.souqApp.domain.main.home.entity.ProductEntity
import com.souqApp.domain.sub_categories.SubCategoryEntity


fun UserResponse.toEntity() = UserEntity(id, name, email, phone, image, verified, token)

fun List<CategoryResponse>.toEntity() = this.map { CategoryEntity(it.id, it.name, it.logo) }

@JvmName("toEntityProductResponse")
fun List<ProductResponse>.toEntity() =
    map {
        ProductEntity(
            it.id,
            it.name,
            it.desc,
            it.smallDesc,
            it.firstImage,
            it.regularPrice,
            it.onSale,
            it.salePrice,
            it.userFavourite,
            it.percentAddedTax,
            it.settingCurrency,
            it.settingPercentAddedTax
        )
    }

@JvmName("toEntitySubCategory")
fun List<SubCategoryResponse>.toEntity() = map { SubCategoryEntity(it.id, it.name, it.logo) }