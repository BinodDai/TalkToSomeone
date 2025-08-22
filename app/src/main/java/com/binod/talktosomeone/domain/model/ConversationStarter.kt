package com.binod.talktosomeone.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ConversationStarter(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconTint: Color,
    val type: ConversationStarterType
)