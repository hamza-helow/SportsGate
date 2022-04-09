package com.souqApp.data.wish_list

import com.souqApp.data.wish_list.remote.WishListApi
import com.souqApp.domain.wish_list.WishListRepository
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
class WishListModule {

    @Provides
    @ViewModelScoped
    fun wishListApiProvide(retrofit: Retrofit): WishListApi {
        return retrofit.create(WishListApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun wishListRepository(wishListApi: WishListApi): WishListRepository {
        return WishListRepositoryImpl(wishListApi)
    }

}