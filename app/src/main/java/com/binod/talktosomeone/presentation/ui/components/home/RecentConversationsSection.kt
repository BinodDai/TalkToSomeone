package com.binod.talktosomeone.presentation.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.binod.talktosomeone.R
import com.binod.talktosomeone.domain.model.RecentChat
import com.binod.talktosomeone.presentation.ui.theme.dimensions

@Composable
fun RecentConversationsSection(recentChats: List<RecentChat>, onItemClick: (RecentChat) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = stringResource(R.string.recent_conversations),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(dimensions.marginMedium))

        Column(verticalArrangement = Arrangement.spacedBy(dimensions.marginSmall)) {
            recentChats.forEach { chat ->
                RecentChatItem(chat = chat, onItemClick = {
                    onItemClick(it)
                })
            }
        }
    }
}
