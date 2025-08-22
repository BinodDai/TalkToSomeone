package com.binod.talktosomeone.data.repository

import com.binod.talktosomeone.data.remote.api.FirestoreService
import com.binod.talktosomeone.domain.model.ChatMessage
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val service: FirestoreService
) : ChatRepository {

    override suspend fun send(message: ChatMessage): Result<Unit> = runCatching {
        service.sendMessage(message)
    }

    override fun observeMessages(chatId: String): Flow<List<ChatMessage>> =
        service.observeMessages(chatId)

    override suspend fun markDelivered(chatId: String, myUserId: String): Result<Unit> =
        runCatching { service.markDelivered(chatId, myUserId) }

    override suspend fun markSeen(chatId: String, myUserId: String): Result<Unit> =
        runCatching { service.markSeen(chatId, myUserId) }

    override suspend fun addReaction(chatId: String, messageId: String, userId: String, emoji: String): Result<Unit> =
        runCatching { service.addReaction(chatId, messageId, userId, emoji) }

    override suspend fun getMessageById(chatId: String, messageId: String): ChatMessage? =
        service.getMessageById(chatId, messageId)

}
