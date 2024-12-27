package com.souqApp.data.products

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.data.products.remote.ProductsApi
import com.souqApp.data.products.remote.dto.AddProductToCartResponse
import com.souqApp.data.products.remote.dto.AddToFavoriteResponse
import com.souqApp.data.products.remote.dto.ProductDetailsResponse
import com.souqApp.data.products.remote.dto.VariationProductPriceInfoResponse
import com.souqApp.domain.products.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val productsApi: ProductsApi) :
    ProductsRepository {

    override suspend fun getProducts(
        type: Int?,
        page: Int?,
        search: String?,
        tag: Int?,
        promo: Int?,
        recommended: Int?,
    ): WrappedListResponse<ProductEntity> {
        return productsApi.getProducts(type, page, search, tag, promo, recommended)
    }


    override suspend fun getProductDetails(productID: Int?): WrappedResponse<ProductDetailsResponse> {
        return productsApi.productDetails(productID)
    }

    override suspend fun addOrRemoveProductToFavorite(
        productId: Int,
        combinationId: Int?
    ): WrappedResponse<AddToFavoriteResponse> {
        return productsApi.addOrRemoveProductToFavorite(productId, combinationId)
    }

    override suspend fun addProductToCart(
        productId: Int,
        combinationId: Int?
    ): WrappedResponse<AddProductToCartResponse> {
        return productsApi.addProductToCart(productId, combinationId)
    }

    override suspend fun getVariationProductPriceInfo(
        productId: Int,
        label: String
    ): WrappedResponse<VariationProductPriceInfoResponse> {
        return productsApi.getVariationProductPriceInfo(productId, label)
    }

    override suspend fun getFavoriteProducts(updated: Long): WrappedListResponse<ProductEntity> {
        return productsApi.getFavoriteProducts(updated)
    }
}