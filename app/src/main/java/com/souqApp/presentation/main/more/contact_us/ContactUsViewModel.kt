package com.souqApp.presentation.main.more.contact_us

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.contact_us.remote.ContactUsRequest
import com.souqApp.domain.contact_us.ContactUsUseCase
import com.souqApp.infra.extension.isEmail
import com.souqApp.infra.extension.isPhone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactUsViewModel @Inject constructor(private val contactUsUseCase: ContactUsUseCase) :
    ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val validateLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }


    fun validate(name: String, email: String, message: String, phoneNumber: String) {
        validateLiveData.value =
            name.isNotBlank() && email.isEmail() && message.isNotBlank() && phoneNumber.isPhone()
    }

    fun sendContactUsInfo(contactUsRequest: ContactUsRequest, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            contactUsUseCase
                .sendContactUs(contactUsRequest)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    onResult(it)
                }
        }
    }

}