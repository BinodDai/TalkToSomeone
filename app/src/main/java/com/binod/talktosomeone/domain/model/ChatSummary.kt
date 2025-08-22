package com.binod.talktosomeone.domain.model

import com.google.firebase.firestore.DocumentId

data class ChatSummary(
    @DocumentId val chatId: String = "",
    val userA: String = "",
    val userB: String = "",
    val lastMessage: String = "",
    val lastTimestamp: Long = 0L,
    val lastSenderId: String = ""
)