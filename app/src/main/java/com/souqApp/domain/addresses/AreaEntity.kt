package com.souqApp.domain.addresses

data class AreaEntity(
    val id: Int,
    val name: String
) {

    override fun toString(): String {
        return name
    }
}