package com.binod.talktosomeone.domain.model

import com.google.firebase.firestore.DocumentId

data class ChatMessage(
    @DocumentId val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String? = null,
    val imageUrl: String? = null,
    val audioUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.SENT,
    val replyToMessageId: String? = null,
    val reactions: Map<String, String> = emptyMap()
)
