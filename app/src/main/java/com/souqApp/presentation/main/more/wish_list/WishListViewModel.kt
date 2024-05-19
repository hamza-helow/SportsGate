package com.souqApp.presentation.main.more.wish_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.wish_list.WishListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(private val wishListUseCase: WishListUseCase) :
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

            wishListUseCase
                .invoke()
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    wishListLiveData.value = it
                }

        }
    }
}