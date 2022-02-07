package com.souqApp.domain.addresses


data class CityEntity(
    val id: Int,
    val name: String,
    val areas: List<AreaEntity>
) {
    override fun toString(): String {
        return name
    }
}