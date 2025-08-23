package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.data.remote.api.FirestoreService
import com.binod.talktosomeone.domain.model.ChatSummary
import com.binod.talktosomeone.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAllChatsUseCase @Inject constructor(
    private val firestoreService: FirestoreService,
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<ChatSummary>> {
        val myId = firestoreService.currentUserId() ?: ""
        return chatRepository.observeAllChats(myId)
    }
}
