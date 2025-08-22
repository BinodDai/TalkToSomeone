package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.model.ChatMessage
import com.binod.talktosomeone.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMessagesUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<List<ChatMessage>> =
        repo.observeMessages(chatId)
}
