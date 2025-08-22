package com.binod.talktosomeone.di

import com.binod.talktosomeone.data.remote.api.GeminiApiService
import com.binod.talktosomeone.data.repository.GeminiRepositoryImpl
import com.binod.talktosomeone.domain.repository.GeminiRepository
import com.binod.talktosomeone.domain.usecase.GenerateTextUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeminiModule {

    @Provides
    @Singleton
    fun provideGeminiApiService(): GeminiApiService {
        return GeminiApiService()
    }

    @Provides
    @Singleton
    fun provideGeminiRepository(
        apiService: GeminiApiService
    ): GeminiRepository {
        return GeminiRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGenerateTextUseCase(
        repository: GeminiRepository
    ): GenerateTextUseCase {
        return GenerateTextUseCase(repository)
    }
}
