package com.souqApp.domain.contact_us

import com.souqApp.data.contact_us.remote.ContactUsRequest
import kotlinx.coroutines.flow.Flow

interface ContactUsRepository {

    suspend fun sendContactUs(contactUsRequest: ContactUsRequest): Flow<Boolean>
}