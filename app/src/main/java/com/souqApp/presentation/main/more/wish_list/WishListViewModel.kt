package com.souqApp.presentation.main.more.wish_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.wish_list.WishListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(private val wishListUseCase: WishListUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<WishListActivityState>()
    val state: LiveData<WishListActivityState> get() = _state

    private fun setLoading(isLoading: Boolean) {
        _state.value = WishListActivityState.Loading(isLoading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = WishListActivityState.Error(throwable)
    }

    private fun onLoaded(products: List<ProductEntity>) {
        _state.value = WishListActivityState.Loaded(products)
    }

    private fun onErrorLoad(response: WrappedListResponse<ProductEntity>) {
        _state.value = WishListActivityState.ErrorLoad(response)
    }

    @Inject
    fun getWishList() {
        viewModelScope.launch {

            wishListUseCase
                .getAll()
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onLoaded(it.data)
                        is BaseResult.Errors -> onErrorLoad(it.error)
                    }
                }

        }
    }
}

sealed class WishListActivityState {

    data class Loading(val isLoading: Boolean) : WishListActivityState()
    data class Error(val throwable: Throwable) : WishListActivityState()
    data class Loaded(val products: List<ProductEntity>) : WishListActivityState()
    data class ErrorLoad(val response: WrappedListResponse<ProductEntity>) : WishListActivityState()
}