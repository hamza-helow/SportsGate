package com.souqApp.data.common.module

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.presentation.main.home.ProductGridAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped


@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {

    @Provides
    @FragmentScoped
    fun productGridAdapterProvide(firebaseConfig: FirebaseRemoteConfig): ProductGridAdapter {
        return ProductGridAdapter(firebaseConfig)
    }

}