package com.souqApp.presentation.main.more.contact_us

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.contact_us.remote.ContactUsRequest
import com.souqApp.domain.contact_us.ContactUsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactUsViewModel @Inject constructor(private val contactUsUseCase: ContactUsUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<ContactUsState>()
    val state: LiveData<ContactUsState> get() = _state

    private fun setLoading(isLoading: Boolean) {
        _state.value = ContactUsState.Loading(isLoading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = ContactUsState.Error(throwable)
    }

    private fun onAdded(isAdded: Boolean) {
        _state.value = ContactUsState.Added(isAdded)
    }

    fun sendContactUsInfo(contactUsRequest: ContactUsRequest){
        viewModelScope.launch {
            contactUsUseCase
                .sendContactUs(contactUsRequest)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    onAdded(it)
                }
        }
    }

}

sealed class ContactUsState {

    data class Loading(val isLoading: Boolean) : ContactUsState()
    data class Error(val throwable: Throwable) : ContactUsState()
    data class Added(val isAdded: Boolean) : ContactUsState()
}