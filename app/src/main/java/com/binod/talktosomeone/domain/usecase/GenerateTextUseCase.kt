package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.repository.GeminiRepository

class GenerateTextUseCase(
    private val repository: GeminiRepository
) {
    suspend operator fun invoke(prompt: String): String {
        return repository.getGeneratedText(prompt)
    }
}
