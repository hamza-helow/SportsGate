package com.souqApp.data.common.module

import android.content.Context
import com.souqApp.infra.utils.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPrefs {
        return SharedPrefs(context)
    }

}


@EntryPoint
@InstallIn(SingletonComponent::class)
interface SharedPrefsEntryPoint {
    val sharedPrefs: SharedPrefs
}
