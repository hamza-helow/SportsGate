package com.souqApp.presentation.main.more.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.GetPagesUseCase
import com.souqApp.domain.settings.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoreViewModel @Inject constructor(
    private val settingsUseCase: SettingsUseCase,
    private val getPagesUseCase: GetPagesUseCase
) :
    ViewModel() {


    private val _state = MutableLiveData<MoreFragmentState>()
    val state: LiveData<MoreFragmentState> get() = _state

    private fun setLoading(isLoading: Boolean) {
        _state.value = MoreFragmentState.Loading(isLoading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = MoreFragmentState.Error(throwable)
    }

    private fun onLoaded(settingEntity: SettingsEntity) {
        _state.value = MoreFragmentState.Loaded(settingEntity)
    }

    private fun onLoadPages(pages: List<PageEntity>) {
        _state.value = MoreFragmentState.Pages(pages)
    }

    private fun onErrorLoad(response: WrappedResponse<SettingsEntity>) {
        _state.value = MoreFragmentState.ErrorLoad(response)
    }

    var facebook: String = ""
    var twitter: String = ""
    var instagram: String = ""
    var tiktok: String = ""


    @Inject
    fun getSettings() {
        viewModelScope.launch {
            settingsUseCase.getSettings()
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


    fun getPages() {
        viewModelScope.launch {
            getPagesUseCase.invoke()
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)

                    when (it) {
                        is BaseResult.Success -> onLoadPages(it.data)
                        is BaseResult.Errors -> Unit
                    }
                }
        }
    }

}


sealed class MoreFragmentState {

    data class Loading(val isLoading: Boolean) : MoreFragmentState()
    data class Error(val throwable: Throwable) : MoreFragmentState()
    data class Loaded(val settingEntity: SettingsEntity) : MoreFragmentState()

    data class Pages(val pages: List<PageEntity>) : MoreFragmentState()

    data class ErrorLoad(val response: WrappedResponse<SettingsEntity>) :
        MoreFragmentState()
}