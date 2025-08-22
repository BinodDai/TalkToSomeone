package com.binod.talktosomeone.domain.repository

interface GeminiRepository {
    suspend fun getGeneratedText(prompt: String): String
}
