package com.souqApp.presentation.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val state = MutableStateFlow<MainActivityState>(MainActivityState.Init)
    val mState: StateFlow<MainActivityState> get() = state

    fun changeNavigation(idNav: Int) {
        state.value = MainActivityState.OnNavigationChanged(idNav)
    }
}


sealed class MainActivityState {

    object Init : MainActivityState()
    data class OnNavigationChanged(val idNav: Int) : MainActivityState()

}