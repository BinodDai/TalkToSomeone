package com.binod.talktosomeone.di

import android.content.Context
import com.binod.talktosomeone.data.local.preferences.LocalStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultStorage

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SecureStorage

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @DefaultStorage
    @Provides
    @Singleton
    fun provideDefaultLocalStorage(@ApplicationContext context: Context): LocalStorage {
        return LocalStorage(
            context = context,
            name = LocalStorage.DEFAULT_PREF_NAME,
            secure = false
        )
    }

    @SecureStorage
    @Provides
    @Singleton
    fun provideSecureLocalStorage(@ApplicationContext context: Context): LocalStorage {
        return LocalStorage(
            context = context,
            name = LocalStorage.DEFAULT_SECURED_PREF_NAME,
            secure = true
        )
    }

}