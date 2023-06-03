package com.souqApp.presentation.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.home.CheckUpdateEntity
import com.souqApp.domain.main.home.CheckUpdateUseCase
import com.souqApp.domain.main.home.HomeEntity
import com.souqApp.domain.main.home.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val checkUpdateUseCase: CheckUpdateUseCase
) : ViewModel() {

    private val state = MutableLiveData<HomeFragmentState>(HomeFragmentState.Init)
    val mState: LiveData<HomeFragmentState> get() = state

    private fun setLoading() {
        state.value = HomeFragmentState.IsLoading(true)
    }

    private fun initState() {
        state.value = HomeFragmentState.Init
    }

    private fun hideLoading() {
        state.value = HomeFragmentState.IsLoading(false)
    }


    private fun homeLoaded(homeEntity: HomeEntity) {
        state.value = HomeFragmentState.HomeLoaded(homeEntity)
    }

    private fun homeErrorLoaded(rawResponse: WrappedResponse<HomeResponse>) {
        state.value = HomeFragmentState.HomeLoadedError(rawResponse)
    }

    @Inject
    fun checkUpdate() {
        viewModelScope.launch {
            checkUpdateUseCase.execute().collect {
                when (it) {
                    is BaseResult.Errors -> Unit
                    is BaseResult.Success -> {
                        state.value = HomeFragmentState.CheckUpdateSuccess(it.data)
                    }
                }
            }
        }

    }

    @Inject
    fun getHome() {
        initState()
        viewModelScope.launch {
            homeUseCase.execute()
                .onStart { setLoading() }
                .catch { hideLoading() }
                .collect {
                    hideLoading()
                    when (it) {
                        is BaseResult.Success -> {
                            state.value =
                                HomeFragmentState.CartCountUpdated(it.data.cartProductsCount)
                            homeLoaded(it.data)
                        }

                        is BaseResult.Errors -> homeErrorLoaded(it.error)
                    }
                }
        }
    }

}

sealed class HomeFragmentState {
    object Init : HomeFragmentState()
    data class IsLoading(val isLoading: Boolean) : HomeFragmentState()
    data class HomeLoaded(val homeEntity: HomeEntity) : HomeFragmentState()
    data class HomeLoadedError(val rawResponse: WrappedResponse<HomeResponse>) : HomeFragmentState()
    data class CartCountUpdated(val count: Int) : HomeFragmentState()

    data class CheckUpdateSuccess(val checkUpdateEntity: CheckUpdateEntity) : HomeFragmentState()
}