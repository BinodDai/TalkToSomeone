package com.binod.talktosomeone.domain.model

data class RecentChat(
    val id: String,
    val name: String,
    val timeAgo: String,
    val online: Boolean = false,
    val avatarText: String,
    val lastMessage: String? = null,
    val unreadCount: Int = 0
)