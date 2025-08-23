package com.binod.talktosomeone.data.repository

import com.binod.talktosomeone.data.remote.api.FirestoreService
import com.binod.talktosomeone.domain.model.ChatMessage
import com.binod.talktosomeone.domain.model.ChatSummary
import com.binod.talktosomeone.domain.model.MessageStatus
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.model.RecentChat
import com.binod.talktosomeone.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val service: FirestoreService
) : ChatRepository {

    override suspend fun send(message: ChatMessage): Result<Unit> = runCatching {
        val chatId = service.chatIdFor(message.senderId, message.receiverId)
        val payload = message.copy(chatId = chatId)

        service.addMessage(chatId, payload)

        // repository handles the summary logic
        val summary = ChatSummary(
            chatId = chatId,
            userA = chatId.substringBefore('_'),
            userB = chatId.substringAfter('_'),
            lastMessage = payload.text ?: (payload.imageUrl?.let { "[image]" } ?: ""),
            lastTimestamp = payload.timestamp,
            lastSenderId = payload.senderId,
            participants = listOf(message.senderId, message.receiverId)
        )
        service.updateChatSummary(summary)
    }

    override fun observeMessages(chatId: String): Flow<List<ChatMessage>> =
        service.observeMessages(chatId)

    override suspend fun markDelivered(chatId: String, myUserId: String): Result<Unit> =
        runCatching {
            val messages = service.findMessagesByStatus(chatId, myUserId, MessageStatus.SENT)
            messages.forEach { service.updateMessageStatus(it.reference, MessageStatus.DELIVERED) }
        }

    override suspend fun markSeen(chatId: String, myUserId: String): Result<Unit> =
        runCatching {
            val messages = service.findMessagesByStatuses(
                chatId,
                myUserId,
                listOf(MessageStatus.SENT, MessageStatus.DELIVERED)
            )
            messages.forEach { service.updateMessageStatus(it.reference, MessageStatus.SEEN) }
        }

    override suspend fun addReaction(
        chatId: String,
        messageId: String,
        userId: String,
        emoji: String
    ): Result<Unit> = runCatching {
        service.updateReaction(chatId, messageId, userId, emoji)
    }

    override suspend fun getMessageById(chatId: String, messageId: String): ChatMessage? =
        service.getMessageById(chatId, messageId)

    override fun observeAllChats(userId: String): Flow<List<ChatSummary>> =
        service.observeMyChats(userId)

}
