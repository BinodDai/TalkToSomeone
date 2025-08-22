package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.repository.ChatRepository
import javax.inject.Inject

class AddReactionUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    suspend operator fun invoke(
        chatId: String,
        messageId: String,
        userId: String,
        emoji: String
    ) = repo.addReaction(chatId, messageId, userId, emoji)
}
