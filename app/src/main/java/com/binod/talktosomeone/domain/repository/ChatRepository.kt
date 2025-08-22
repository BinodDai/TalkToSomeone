package com.binod.talktosomeone.domain.repository

import com.binod.talktosomeone.domain.model.ChatMessage
import com.binod.talktosomeone.domain.model.RecentChat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun send(message: ChatMessage): Result<Unit>
    fun observeMessages(chatId: String): Flow<List<ChatMessage>>
    suspend fun markDelivered(chatId: String, myUserId: String): Result<Unit>
    suspend fun markSeen(chatId: String, myUserId: String): Result<Unit>
    suspend fun addReaction(
        chatId: String,
        messageId: String,
        userId: String,
        emoji: String
    ): Result<Unit>

    suspend fun getMessageById(chatId: String, messageId: String): ChatMessage?
    fun observeRecentChats(userId: String): Flow<List<RecentChat>>
}