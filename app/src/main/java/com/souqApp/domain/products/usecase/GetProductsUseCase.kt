package com.souqApp.domain.products.usecase

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.products.ProductsRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val ordersRepository: ProductsRepository) {
    val request = GetProductsRequest()

    fun invoke(): LiveData<PagingData<ProductEntity>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            ProductsPagingSource(
                productsRepository = ordersRepository,
                tag = request.tag,
                categoryId = request.categoryId,
                promo = request.promo,
                search = request.search,
                recommended = request.recommended
            )
        }
    ).liveData
}
