package com.souqApp.data.common.mapper

import com.souqApp.data.addresses.remote.dto.AddressDetailsResponse
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.addresses.remote.dto.CityResponse
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductQtyResponse
import com.souqApp.data.orders.remote.OrderResponse
import com.souqApp.data.checkout_details.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.checkout_details.remote.dto.DeliveryOptionResponse
import com.souqApp.data.product_details.remote.VariationProductPriceInfoResponse
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
import com.souqApp.domain.orders.OrderEntity
import com.souqApp.domain.checkout_details.CheckoutDetailsEntity
import com.souqApp.domain.checkout_details.DeliveryOptionEntity
import com.souqApp.domain.product_details.VariationProductPriceInfoEntity
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
    subTotal = subTotal,
    valueAddedTax = valueAddedTax,
    deliveryPrice = deliveryPrice,
    couponDiscount = couponDiscount,
    totalPrice = totalPrice,
    settingCurrency = settingCurrency,
    userAddress = userAddress?.toEntity(),
    hasDelivery = hasDelivery == true,
    hasDiscount = hasDiscount == true,
    hasTax = hasTax == true,
    deliveryOptions = availableDeliveryOptions.toEntity(),
    deliveryOptionId = deliveryOptionId ?: 1
)


fun VariationProductPriceInfoResponse.toEntity(): VariationProductPriceInfoEntity {
    return VariationProductPriceInfoEntity(
        combinationId = combinationId ?: 0,
        discountPercentage = discountPercentage ?: 0.0,
        discountPrice = discountPrice ?: "-",
        lastPrice = lastPrice ?: "-",
        price = price ?: "-",
        qty = qty ?: 0,
        currency = currency ?: "-"
    )
}

@JvmName("toDeliveryOptionEntity")
fun List<DeliveryOptionResponse>.toEntity() = map {
    DeliveryOptionEntity(
        id = it.id ?: 0,
        name = it.name ?: "-",
        price = it.price ?: "-"
    )
}


@JvmName("toEntitySubCategory")
fun List<SubCategoryResponse>.toEntity() = map { SubCategoryEntity(it.id, it.name, it.logo) }

@JvmName("toEntityAddressResponse")
fun List<AddressResponse>.toEntity() = map { AddressEntity(it.id, it.fullAddress, it.isDefault) }

fun AddressResponse.toEntity() = AddressEntity(id, fullAddress, isDefault)


fun CartDetailsResponse.toEntity() =
    CartDetailsEntity(subTotal, currency, products)

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