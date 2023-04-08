package com.souqApp.data.checkout_details

import com.souqApp.data.checkout_details.remote.PaymentDetailsApi
import com.souqApp.data.checkout_details.remote.PaymentDetailsRepositoryImpl
import com.souqApp.domain.checkout_details.PaymentDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class PaymentDetailsModule {

    @Provides
    @ViewModelScoped
    fun paymentDetailsApiProvide(retrofit: Retrofit): PaymentDetailsApi {
        return retrofit.create(PaymentDetailsApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun paymentDetailsRepositoryProvide(paymentDetailsApi: PaymentDetailsApi): PaymentDetailsRepository {
        return PaymentDetailsRepositoryImpl(paymentDetailsApi)
    }
}