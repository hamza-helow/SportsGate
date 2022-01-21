package com.souqApp.domain.main.home.entity

import java.io.Serializable

data class CategoryEntity(
    val id: Int,
    val name: String?,
    val logo: String?,
) : Serializable