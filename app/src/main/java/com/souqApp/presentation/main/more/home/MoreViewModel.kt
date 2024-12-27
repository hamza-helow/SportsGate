package com.souqApp.presentation.main.more.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.usecase.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(private val settingsUseCase: SettingsUseCase) :
    ViewModel() {

    val settingsLiveData: MutableLiveData<BaseResult<SettingsEntity, WrappedResponse<SettingsEntity>>> =
        MutableLiveData()

    var facebook: String = ""
    var twitter: String = ""
    var instagram: String = ""
    var tiktok: String = ""


    @Inject
    fun getSettings() {
        viewModelScope.launch {
            settingsUseCase.invoke()
                .catch {}
                .collect { settingsLiveData.value = it }
        }
    }

}
