package com.binod.talktosomeone.data.remote.api

import com.binod.talktosomeone.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel

class GeminiApiService {
    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro-002",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun generateText(prompt: String): String {
        val response = generativeModel.generateContent(prompt)
        return response.text ?: ""
    }
}
