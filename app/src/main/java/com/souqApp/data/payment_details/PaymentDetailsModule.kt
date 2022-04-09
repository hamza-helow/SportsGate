package com.souqApp.data.payment_details

import com.souqApp.data.payment_details.remote.PaymentDetailsApi
import com.souqApp.data.payment_details.remote.PaymentDetailsRepositoryImpl
import com.souqApp.domain.payment_details.PaymentDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

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