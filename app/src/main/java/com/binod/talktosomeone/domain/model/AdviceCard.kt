package com.binod.talktosomeone.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class AdviceCard(
    val title: String,
    val des: String,
    val icon: ImageVector,
    val iconTintColor: Color,
    val backgroundColor: Color,
)
