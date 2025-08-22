package com.binod.talktosomeone.presentation.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.binod.talktosomeone.domain.model.ConversationStarter
import com.binod.talktosomeone.domain.model.ConversationStarterType
import com.binod.talktosomeone.presentation.ui.theme.dimensions

@Composable
fun ConversationStartersSection(
    starters: List<ConversationStarter>,
    selectedType: ConversationStarterType?,
    onItemClick: (ConversationStarterType) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.start_a_conversation),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(dimensions.paddingMedium))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(dimensions.paddingMedium)
        ) {
            starters.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.paddingSmall)
                ) {
                    rowItems.forEach { starter ->
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            ConversationStarterCard(
                                starter = starter,
                                isSelected = starter.type == selectedType,
                                onItemClick = {
                                    onItemClick(it)
                                })
                        }
                    }
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}