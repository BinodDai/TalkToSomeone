package com.binod.talktosomeone.data.repository

import com.binod.talktosomeone.data.remote.api.GeminiApiService
import com.binod.talktosomeone.domain.repository.GeminiRepository

class GeminiRepositoryImpl(
    private val apiService: GeminiApiService
) : GeminiRepository {

    override suspend fun getGeneratedText(prompt: String): String {
        return apiService.generateText(prompt)
    }
}
