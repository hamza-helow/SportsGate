package com.souqApp.presentation.main.more.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.usecase.GetPagesUseCase
import com.souqApp.domain.settings.usecase.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val settingsUseCase: SettingsUseCase,
    private val getPagesUseCase: GetPagesUseCase
) :
    ViewModel() {

    val settingsLiveData: MutableLiveData<BaseResult<SettingsEntity, WrappedResponse<SettingsEntity>>> =
        MutableLiveData()

    var facebook: String = ""
    var twitter: String = ""
    var instagram: String = ""
    var tiktok: String = ""

    val pagesLiveData: MutableLiveData<BaseResult<List<PageEntity>, WrappedListResponse<PageEntity>>> =
        MutableLiveData()

    @Inject
    fun getPages() {
        viewModelScope.launch {
            getPagesUseCase.invoke()
                .catch { }
                .collect {
                    pagesLiveData.value = it
                }
        }
    }

    @Inject
    fun getSettings() {
        viewModelScope.launch {
            settingsUseCase.invoke()
                .catch {}
                .collect { settingsLiveData.value = it }
        }
    }

}
