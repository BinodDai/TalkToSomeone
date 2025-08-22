package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.model.ChatMessage
import com.binod.talktosomeone.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    suspend operator fun invoke(message: ChatMessage) = repo.send(message)
}