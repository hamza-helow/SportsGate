package com.souqApp.domain.contact_us

import com.souqApp.data.contact_us.remote.ContactUsRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactUsUseCase @Inject constructor(private val contactUsRepository: ContactUsRepository) {

    suspend fun sendContactUs(contactUsRequest: ContactUsRequest): Flow<Boolean> {
        return contactUsRepository.sendContactUs(contactUsRequest)
    }
}