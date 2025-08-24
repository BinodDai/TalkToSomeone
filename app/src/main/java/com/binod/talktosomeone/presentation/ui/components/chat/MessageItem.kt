package com.binod.talktosomeone.presentation.ui.components.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binod.talktosomeone.domain.model.ChatMessage
import com.binod.talktosomeone.domain.model.MessageStatus
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.presentation.ui.theme.PrimaryLight
import com.binod.talktosomeone.presentation.ui.theme.Shapes
import com.binod.talktosomeone.presentation.ui.theme.dimensions
import com.binod.talktosomeone.utils.copyTextToClipboard
import com.binod.talktosomeone.utils.formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageItem(
    message: ChatMessage,
    currentUserId: String,
    partnerProfile: Profile? = null,
    repliedMessage: ChatMessage? = null,
    onReplySwipe: ((ChatMessage) -> Unit)? = null
) {
    val isFromMe = message.senderId == currentUserId
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val swipeThresholdPx = with(density) { 56.dp.toPx() }
    var totalDragX by remember(message.id) { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.paddingMedium)
            .pointerInput(message.id) {
                detectHorizontalDragGestures(
                    onDragStart = { totalDragX = 0f },
                    onHorizontalDrag = { change, dragAmount ->
                        totalDragX += dragAmount
                        change.consume()
                    },
                    onDragCancel = { totalDragX = 0f },
                    onDragEnd = {
                        when {
                            // incoming message: swipe right to reply
                            !isFromMe && totalDragX >= swipeThresholdPx ->
                                onReplySwipe?.invoke(message)

                            // outgoing message: swipe left to reply
                            isFromMe && totalDragX <= -swipeThresholdPx ->
                                onReplySwipe?.invoke(message)
                        }
                        totalDragX = 0f
                    }
                )
            }

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isFromMe) Arrangement.End else Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .combinedClickable(
                        onClick = {

                        },
                        onLongClick = {
                            copyTextToClipboard(context, message.text ?: "")
                        }
                    ),
                horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
            ) {
                Box {
                    Column {
                        message.replyToMessageId?.let { replyMessageId ->
                            Card(
                                modifier = Modifier
                                    .padding(bottom = dimensions.paddingExtraSmall),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                        alpha = 0.5f
                                    )
                                ),
                                shape = Shapes.medium
                            ) {
                                Column(
                                    modifier = Modifier.padding(dimensions.paddingMedium)
                                ) {
                                    Text(
                                        text = if (isFromMe) "You" else (partnerProfile?.codeName
                                            ?: "User"),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = PrimaryLight
                                    )
                                    Text(
                                        text = repliedMessage?.text?.ifBlank { "Audio message" } ?: "Message not found",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Main message bubble
                        Card(
                            modifier = Modifier,
                            colors = CardDefaults.cardColors(
                                containerColor = if (isFromMe)
                                    PrimaryLight else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(
                                topStart = if (isFromMe) dimensions.paddingMedium else dimensions.paddingExtraSmall,
                                topEnd = if (isFromMe) dimensions.paddingExtraSmall else dimensions.paddingMedium,
                                bottomStart = dimensions.paddingMedium,
                                bottomEnd = dimensions.paddingMedium
                            )
                        ) {

                            if (message.audioUrl?.isNotEmpty() == true) {
                                AudioMessageContent(
                                    duration = "5000L",
                                    isFromMe = isFromMe
                                )
                            }

                            if (message.text?.isNotEmpty() == true) {
                                Text(
                                    text = message.text,
                                    modifier = Modifier.padding(dimensions.paddingMedium),
                                    color = if (isFromMe) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        // Timestamp + Status Ticks
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.padding(
                                horizontal = dimensions.paddingExtraSmall,
                                vertical = dimensions.paddingExtraExtraSmall
                            )
                        ) {
                            Text(
                                text = formatTime(message.timestamp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (isFromMe) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = when (message.status) {
                                        MessageStatus.SENT -> Icons.Default.Check        // single tick
                                        MessageStatus.DELIVERED -> Icons.Default.DoneAll // double tick
                                        MessageStatus.SEEN -> Icons.Default.DoneAll      // double tick, blue
                                    },
                                    contentDescription = "Message status",
                                    modifier = Modifier.size(16.dp),
                                    tint = when (message.status) {
                                        MessageStatus.SEEN -> Color.Blue
                                        else -> Color.Gray
                                    }
                                )
                            }
                        }

                    }

                    if (message.reactions.isNotEmpty()) {
                        val reaction = message.reactions
                        Card(
                            modifier = Modifier
                                .align(
                                    if (isFromMe) Alignment.BottomStart
                                    else Alignment.BottomEnd
                                )
                                .offset(
                                    x = if (isFromMe) (-8).dp else 8.dp,
                                    y = (-8).dp
                                )
                                .clickable { /* Handle reaction click */ },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = reaction.values.first(),
                                modifier = Modifier.padding(dimensions.paddingSmall),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}