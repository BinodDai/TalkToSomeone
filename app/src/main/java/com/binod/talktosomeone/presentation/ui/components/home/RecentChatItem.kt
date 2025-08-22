package com.binod.talktosomeone.presentation.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.binod.talktosomeone.domain.model.RecentChat
import com.binod.talktosomeone.presentation.ui.theme.BackgroundDark
import com.binod.talktosomeone.presentation.ui.theme.BackgroundLight
import com.binod.talktosomeone.presentation.ui.theme.GradientEnd
import com.binod.talktosomeone.presentation.ui.theme.GradientStart
import com.binod.talktosomeone.presentation.ui.theme.Gray200
import com.binod.talktosomeone.presentation.ui.theme.Gray400
import com.binod.talktosomeone.presentation.ui.theme.Gray500
import com.binod.talktosomeone.presentation.ui.theme.OnlineIndicator
import com.binod.talktosomeone.presentation.ui.theme.Shapes
import com.binod.talktosomeone.presentation.ui.theme.dimensions
import com.binod.talktosomeone.utils.isLightTheme

@Composable
fun RecentChatItem(chat: RecentChat, onItemClick: (RecentChat) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 0.5.dp,
                color = Gray200,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onItemClick(chat) }
            .padding(dimensions.paddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(GradientStart, GradientEnd)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.avatarText,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.width(dimensions.paddingMedium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(dimensions.paddingSmall))
            Text(
                text = chat.timeAgo,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isLightTheme()) Gray500 else Gray400
            )
        }

        if (chat.isOnline) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(OnlineIndicator, CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (isLightTheme()) BackgroundLight else BackgroundDark,
                        shape = CircleShape
                    )
            )
        }
    }
}


