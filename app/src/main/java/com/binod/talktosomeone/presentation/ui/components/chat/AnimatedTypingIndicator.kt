package com.binod.talktosomeone.presentation.ui.components.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.binod.talktosomeone.presentation.ui.theme.PrimaryLight
import com.binod.talktosomeone.presentation.ui.theme.Shapes
import com.binod.talktosomeone.presentation.ui.theme.dimensions
import kotlinx.coroutines.delay

@Composable
fun AnimatedTypingIndicator(
    isVisible: Boolean = true,
    userName: String = ""
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = dimensions.paddingMedium, vertical = dimensions.paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User avatar (small)
            Box(
                modifier = Modifier
                    .size(dimensions.spaceLarge)
                    .clip(CircleShape)
                    .background(PrimaryLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(2).uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(dimensions.marginMedium))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = Shapes.medium
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = dimensions.paddingMedium,
                        vertical = dimensions.paddingSmall
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Animated dots
                    repeat(3) { index ->
                        val infiniteTransition = rememberInfiniteTransition(label = "typing")
                        val alpha by infiniteTransition.animateFloat(
                            initialValue = 0.3f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(600),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "dot_$index"
                        )

                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
                        )

                        // Add delay between dots
                        LaunchedEffect(Unit) {
                            delay(index * 200L)
                        }
                    }
                }
            }
        }
    }
}