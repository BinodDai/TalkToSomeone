package com.binod.talktosomeone.domain.model

data class MessageReaction(
    val emoji: String,
    val userId: String,
    val userName: String
)