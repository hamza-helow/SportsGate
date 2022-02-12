package com.souqApp.data.common.mapper

import com.souqApp.data.addresses.remote.dto.AddressDetailsResponse
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.addresses.remote.dto.CityResponse
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductQtyResponse
import com.souqApp.data.main.common.CategoryResponse
import com.souqApp.data.main.home.remote.dto.ProductResponse
import com.souqApp.data.orders.remote.OrderResponse
import com.souqApp.data.payment_details.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.product_details.remote.ProductDetailsResponse
import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeResponse
import com.souqApp.data.sub_categories.remote.dto.SubCategoryResponse
import com.souqApp.domain.addresses.AddressDetailsEntity
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.domain.addresses.AreaEntity
import com.souqApp.domain.addresses.CityEntity
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import com.souqApp.domain.main.cart.entity.UpdateProductQtyEntity
import com.souqApp.domain.main.home.entity.CategoryEntity
import com.souqApp.domain.main.home.entity.ProductEntity
import com.souqApp.domain.orders.OrderEntity
import com.souqApp.domain.payment_details.CheckoutDetailsEntity
import com.souqApp.domain.product_details.ProductDetailsEntity
import com.souqApp.domain.products_by_type.ProductsByTypeEntity
import com.souqApp.domain.sub_categories.SubCategoryEntity


fun UserResponse.toEntity() = UserEntity(id, name, email, phone, image, verified, token)

fun ProductsByTypeResponse.toEntity() = ProductsByTypeEntity(hasMore, products)

fun UpdateProductQtyResponse.toEntity() =
    UpdateProductQtyEntity(subTotal, settingCurrency, cartProductsCount)

fun CheckoutResponse.toEntity() = CheckoutEntity(orderId)

fun AddressDetailsResponse.toEntity() = AddressDetailsEntity(
    areaId,
    areaName,
    building,
    cityId,
    cityName,
    floor,
    id,
    lat,
    lng,
    notes,
    street
)


fun CheckoutDetailsResponse.toEntity() = CheckoutDetailsEntity(
    userDefaultAddress,
    userDefaultAddressId,
    subTotal,
    valueAddedTax,
    deliveryPrice,
    couponDiscount,
    totalPrice,
    settingCurrency
)

fun ProductDetailsResponse.toEntity() = ProductDetailsEntity(
    id,
    name,
    desc,
    regularPrice,
    onSale,
    salePrice,
    discount,
    stock,
    userFavourite,
    percentAddedTax,
    settingCurrency,
    settingPercentAddedTax,
    images,
    relevant
)

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

@JvmName("toEntityAddressResponse")
fun List<AddressResponse>.toEntity() = map { AddressEntity(it.id, it.fullAddress, it.isDefault) }


fun CartDetailsResponse.toEntity() =
    CartDetailsEntity(subTotal, settingCurrency, allAvailableInStock, products)

@JvmName("toEntityCityResponse")
fun List<CityResponse>.toEntity() =
    map { CityEntity(it.id, it.name, it.areas.map { area -> AreaEntity(area.id, area.name) }) }


@JvmName("toEntityOrderResponse")
fun List<OrderResponse>.toEntity() =
    map {
        OrderEntity(
            it.createdAt,
            it.id,
            it.number,
            it.settingCurrency,
            it.status,
            it.totalPrice
        )
    }