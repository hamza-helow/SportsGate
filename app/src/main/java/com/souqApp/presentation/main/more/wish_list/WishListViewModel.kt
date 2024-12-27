package com.souqApp.presentation.main.more.wish_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.data.products.remote.dto.AddToFavoriteResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.usecase.AddOrRemoveProductToFavoriteUseCase
import com.souqApp.domain.products.usecase.GetFavoriteProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    private val addOrRemoveProductToFavoriteUseCase: AddOrRemoveProductToFavoriteUseCase
) :
    ViewModel() {


    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val wishListLiveData: MutableLiveData<BaseResult<List<ProductEntity>, WrappedListResponse<ProductEntity>>> =
        MutableLiveData()

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }

    @Inject
    fun getWishList() {
        viewModelScope.launch {
            getFavoriteProductsUseCase
                .invoke()
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    wishListLiveData.value = it
                }

        }
    }

    fun removeProductFromFavorite(
        productId: Int?,
        onCollect: (BaseResult<AddToFavoriteResponse, WrappedResponse<AddToFavoriteResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            addOrRemoveProductToFavoriteUseCase
                .invoke(productId)
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    onCollect(it)
                }
        }
    }
}