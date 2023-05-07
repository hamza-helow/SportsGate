package com.souqApp.data.common.mapper

import com.souqApp.data.addresses.remote.dto.AddressDetailsResponse
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.addresses.remote.dto.CityResponse
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.Constants
import com.souqApp.data.main.cart.remote.dto.*
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.data.orders.remote.OrderDetailsResponse
import com.souqApp.data.orders.remote.OrderResponse
import com.souqApp.data.orders.remote.ProductInOrderResponse
import com.souqApp.data.product_details.remote.AddProductToCartResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.data.product_details.remote.ProductDetailsResponse
import com.souqApp.data.product_details.remote.VariationProductPriceInfoResponse
import com.souqApp.domain.addresses.AddressDetailsEntity
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.domain.addresses.AreaEntity
import com.souqApp.domain.addresses.CityEntity
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.main.cart.entity.*
import com.souqApp.domain.main.home.HomeEntity
import com.souqApp.domain.orders.OrderDetailsEntity
import com.souqApp.domain.orders.OrderEntity
import com.souqApp.domain.orders.ProductInOrderEntity
import com.souqApp.domain.product_details.AddProductToCartEntity
import com.souqApp.domain.product_details.VariationProductPriceInfoEntity
import com.souqApp.infra.extension.orDash

fun UserResponse.toEntity() = UserEntity(id, name, email, phone, image, verified, token)


fun UpdateProductCartResponse.toEntity() = UpdateProductCartEntity(
    cartItemId = cartItemId ?: Constants.UNDEFINED_ID,
    subTotal = subTotal.orDash(),
    productQty = updatedQty ?: 0
)

fun CheckoutResponse.toEntity() = CheckoutEntity(orderId)

fun AddressDetailsResponse.toEntity() = AddressDetailsEntity(
    areaId, areaName, building, cityId, cityName, floor, id, lat, lng, notes, street
)


fun AddProductToCartResponse.toEntity() = AddProductToCartEntity(
    productsCount = productsCount ?: 0
)

fun HomeResponse.toEntity() = HomeEntity(
    cartProductsCount ?: 0,
    promotions.orEmpty(),
    categories.orEmpty(),
    newProducts.orEmpty(),
    bestSellingProducts.orEmpty(),
    recommendedProducts.orEmpty(),
    tags.orEmpty()
)

fun ProductDetailsResponse.toEntity() = ProductDetailsEntity(
    id = id ?: Constants.UNDEFINED_ID,
    name = name.orDash(),
    desc = desc.orEmpty(),
    price = price.orDash(),
    discountPrice = discountPrice.orDash(),
    discountPercentage = discountPercentage ?: 0.0,
    onSale = isSalesable ?: false,
    qty = qty ?: 0,
    media = media,
    relevant = relevant,
    variations = variations,
    combinationOptions = combinationOptions,
    variationCompainationId = variationCompainationId,
    isFavorite = isFavorite ?: false,
    tags = tags
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


fun OrderDetailsResponse.toEntity(): OrderDetailsEntity {
    return OrderDetailsEntity(
        address = address.orDash(),
        couponDiscount = couponDiscount.orDash(),
        couponPercent = couponPercent ?: 0.0,
        deliveryOptionId = deliveryOptionId ?: 0,
        deliveryPrice = deliveryPrice.orDash(),
        orderNumber = orderNumber.orDash(),
        products = products.toEntities(),
        reason = reason.orDash(),
        status = status ?: 0,
        statusDescription = statusDescription.orDash(),
        subTotal = subTotal.orDash(),
        total = total.orDash(),
        vat = vat.orDash()
    )
}

@JvmName("toProductInOrderEntities")
fun List<ProductInOrderResponse>.toEntities(): List<ProductInOrderEntity> {
    return map {
        ProductInOrderEntity(
            discountPrice = it.discountPrice.orDash(),
            id = it.id ?: 0,
            lastPrice = it.lastPrice.orDash(),
            name = it.name.orDash(),
            qty = it.qty ?: 1,
            thumb = it.thumb.orEmpty(),
            total_price = it.total_price.orDash(),
            variation_compaination_id = it.variation_compaination_id ?: 0,
            variation_compaination_label = it.variation_compaination_label.orDash()
        )
    }
}

@JvmName("toDeliveryOptionEntity")
fun List<DeliveryOptionResponse>.toEntity() = map {
    DeliveryOptionEntity(
        id = it.id ?: 0, name = it.name ?: "-", price = it.price ?: "-"
    )
}

@JvmName("toEntityAddressResponse")
fun List<AddressResponse>.toEntity() = map { AddressEntity(it.id, it.fullAddress, it.isDefault) }

fun AddressResponse.toEntity() = AddressEntity(id, fullAddress, isDefault)


fun CartDetailsResponse.toEntity() = CartDetailsEntity(subTotal, currency, products.toEntities())

@JvmName("toProductsInCartEntities")
fun List<ProductInCartResponse>.toEntities(): List<ProductInCartEntity> {
    return map {
        ProductInCartEntity(
            id = it.id,
            cartItemId = it.cartItemId ?: Constants.UNDEFINED_ID,
            it.name,
            it.thumb,
            it.totalPrice,
            it.qty,
            it.combinationId
        )
    }
}

@JvmName("toEntityCityResponse")
fun List<CityResponse>.toEntity() =
    map { CityEntity(it.id, it.name, it.areas.map { area -> AreaEntity(area.id, area.name) }) }


@JvmName("toEntityOrderResponse")
fun List<OrderResponse>.toEntity() = map {
    OrderEntity(
        it.createdAt.orDash(),
        it.id ?: 0,
        it.number.orDash(),
        it.statusDescription.orDash(),
        it.totalPrice.orDash(),
    )
}