package com.souqApp.presentation.products

import com.souqApp.data.main.home.remote.dto.ProductEntity

sealed class ProductsFragmentState {

    data class Loading(val show: Boolean) : ProductsFragmentState()
    data class OnProductsLoaded(val result: List<ProductEntity>) : ProductsFragmentState()
}