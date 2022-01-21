package com.souqApp.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.home.HomeUseCase
import com.souqApp.domain.main.home.entity.HomeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeUseCase: HomeUseCase) : ViewModel() {

    private val state = MutableStateFlow<HomeFragmentState>(HomeFragmentState.Init)
    val mState: StateFlow<HomeFragmentState> get() = state

    private fun setLoading() {
        state.value = HomeFragmentState.IsLoading(true)
    }

    private fun initState() {
        state.value = HomeFragmentState.Init
    }

    private fun hideLoading() {
        state.value = HomeFragmentState.IsLoading(false)
    }

    private fun showToast(error: Throwable) {
        state.value = HomeFragmentState.Error(error)
    }

    private fun homeLoaded(homeEntity: HomeEntity) {
        state.value = HomeFragmentState.HomeLoaded(homeEntity)
    }

    private fun homeErrorLoaded(rawResponse: WrappedResponse<HomeResponse>) {
        state.value = HomeFragmentState.HomeLoadedError(rawResponse)
    }


    @Inject
    fun getHome() {
        initState()
        viewModelScope.launch {
            homeUseCase.home()
                .onStart { setLoading() }
                .catch {
                    hideLoading()
                    showToast(it)
                }
                .collect {
                    hideLoading()

                    when (it) {
                        is BaseResult.Success -> homeLoaded(it.data)
                        is BaseResult.Errors -> homeErrorLoaded(it.error)
                    }
                }
        }
    }

}

sealed class HomeFragmentState {
    object Init : HomeFragmentState()
    data class IsLoading(val isLoading: Boolean) : HomeFragmentState()
    data class Error(val error: Throwable) : HomeFragmentState()
    data class HomeLoaded(val homeEntity: HomeEntity) : HomeFragmentState()
    data class HomeLoadedError(val rawResponse: WrappedResponse<HomeResponse>) : HomeFragmentState()

}