package com.souqApp.domain.settings.usecase

import com.souqApp.data.settings.remote.dto.ContactUsRequest
import com.souqApp.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactUsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend fun invoke(contactUsRequest: ContactUsRequest): Flow<Boolean> {
        return flow {
            val response = settingsRepository.sendContactUs(contactUsRequest)
            val isSuccessful = response.status
            emit(isSuccessful)
        }
    }
}