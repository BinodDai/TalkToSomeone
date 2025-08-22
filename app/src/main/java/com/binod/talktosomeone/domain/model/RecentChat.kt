package com.binod.talktosomeone.domain.model

data class RecentChat(
    val chatId: String,
    val userId: String,
    val name: String,
    val timeAgo: String,
    val online: Boolean = false,
    val avatarText: String,
    val lastMessage: String? = null,
    val unreadCount: Int = 0
)