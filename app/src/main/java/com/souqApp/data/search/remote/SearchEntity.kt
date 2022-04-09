package com.souqApp.data.search.remote

import com.souqApp.data.main.home.remote.dto.ProductEntity

data class SearchEntity(
    val has_more: Boolean,
    val pagination_num: Int,
    val products: List<ProductEntity>
)



