package com.binod.talktosomeone.presentation.ui.components.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.binod.talktosomeone.domain.model.ConversationStarter
import com.binod.talktosomeone.domain.model.ConversationStarterType
import com.binod.talktosomeone.presentation.ui.theme.AdviceColor
import com.binod.talktosomeone.presentation.ui.theme.PrimaryLight
import com.binod.talktosomeone.presentation.ui.theme.Shapes
import com.binod.talktosomeone.presentation.ui.theme.TopicColor
import com.binod.talktosomeone.presentation.ui.theme.VentColor
import com.binod.talktosomeone.presentation.ui.theme.dimensions
import com.binod.talktosomeone.utils.Constants
import com.binod.talktosomeone.utils.getConversationStarterType

@Composable
fun ConversationStarterCard(
    starter: ConversationStarter,
    isSelected: Boolean,
    onItemClick: (ConversationStarterType) -> Unit
) {

    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surface

    val textColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    val subtitleColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
    else
        MaterialTheme.colorScheme.onSurfaceVariant

    val iconBackgroundColor = when (starter.title) {
        Constants.QUICK_MATCH -> PrimaryLight
        Constants.VENT -> VentColor
        Constants.ADVICE -> AdviceColor
        Constants.TOPIC -> TopicColor
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onItemClick(getConversationStarterType(starter.title))
            },
        shape = Shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensions.paddingMedium),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        iconBackgroundColor,
                        shape = Shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = starter.icon,
                    contentDescription = starter.title,
                    tint = starter.iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(dimensions.marginSmall))
                Text(
                    text = starter.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(dimensions.marginSmall))
                Text(
                    text = starter.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtitleColor,
                )
            }
        }
    }
}