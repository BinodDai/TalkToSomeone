package com.binod.talktosomeone.presentation.ui.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun IconCircleButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        shape = CircleShape,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Box(contentAlignment = Alignment.Center) {
            icon()
        }
    }
}