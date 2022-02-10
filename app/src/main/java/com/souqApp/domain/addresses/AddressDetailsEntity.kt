package com.souqApp.domain.addresses

import java.io.Serializable

data class AddressDetailsEntity(
    val area_id: Int,
    val area_name: String,
    val building: String,
    val city_id: Int,
    val city_name: String,
    val floor: String,
    val id: Int,
    val lat: Double,
    val lng: Double,
    val notes: String,
    val street: String
) : Serializable