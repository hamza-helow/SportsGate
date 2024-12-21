package com.souqApp.data.contact_us

import com.souqApp.data.contact_us.remote.ContactUsApi
import com.souqApp.data.contact_us.remote.ContactUsRequest
import com.souqApp.domain.contact_us.ContactUsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactUsRepositoryImpl @Inject constructor(private val contactUsApi: ContactUsApi) :
    ContactUsRepository {
    override suspend fun sendContactUs(contactUsRequest: ContactUsRequest): Flow<Boolean> {
        return flow {
            val response = contactUsApi.sendContactUs(contactUsRequest)
            val isSuccessful = response.status
            emit(isSuccessful)
        }
    }
}