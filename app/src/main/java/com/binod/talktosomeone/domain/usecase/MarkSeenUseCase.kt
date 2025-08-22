package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.repository.ChatRepository
import javax.inject.Inject

class MarkSeenUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    suspend operator fun invoke(chatId: String, myUserId: String) =
        repo.markSeen(chatId, myUserId)
}
