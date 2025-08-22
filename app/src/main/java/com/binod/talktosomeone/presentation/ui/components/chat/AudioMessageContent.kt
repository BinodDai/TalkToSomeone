package com.binod.talktosomeone.presentation.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AudioMessageContent(
    duration: String,
    isFromMe: Boolean
) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .widthIn(min = 200.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Play button
        IconButton(
            onClick = { /* Handle audio play */ },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = if (isFromMe) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Audio waveform visualization (simplified)
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(12) { index ->
                val height = listOf(8, 12, 6, 14, 10, 8, 16, 12, 8, 10, 6, 8)[index]
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(height.dp)
                        .background(
                            color = if (isFromMe) Color.White.copy(alpha = 0.7f)
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(1.5.dp)
                        )
                )
            }
        }

        // Duration
        Text(
            text = duration,
            fontSize = 12.sp,
            color = if (isFromMe) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}