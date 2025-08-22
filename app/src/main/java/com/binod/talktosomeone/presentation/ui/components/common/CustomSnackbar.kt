package com.binod.talktosomeone.presentation.ui.components.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun CustomSnackbar(
    snackbarState: SnackbarState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    duration: Long = 5000L,
    isDarkTheme: Boolean = false
) {
    LaunchedEffect(snackbarState.isVisible) {
        if (snackbarState.isVisible) {
            delay(duration)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = snackbarState.isVisible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(300, easing = EaseOutBack)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(250)
        ) + fadeOut(animationSpec = tween(250)),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = getSnackbarColors(snackbarState.type, isDarkTheme).background
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = getSnackbarIcon(snackbarState.type),
                    contentDescription = null,
                    tint = getSnackbarColors(snackbarState.type, isDarkTheme).icon,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = snackbarState.message,
                    color = getSnackbarColors(snackbarState.type, isDarkTheme).text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = getSnackbarColors(snackbarState.type, isDarkTheme).text,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

enum class SnackbarType {
    SUCCESS, ERROR, WARNING
}

data class SnackbarState(
    val message: String = "",
    val type: SnackbarType = SnackbarType.SUCCESS,
    val isVisible: Boolean = false
)

private data class SnackbarColors(
    val background: Color,
    val text: Color,
    val icon: Color
)

private fun getSnackbarColors(type: SnackbarType, isDarkTheme: Boolean): SnackbarColors {
    return if (isDarkTheme) {
        when (type) {
            SnackbarType.SUCCESS -> SnackbarColors(
                background = Color(0xFF0D4F3C),
                text = Color(0xFF86EFAC),
                icon = Color(0xFF22C55E)
            )

            SnackbarType.ERROR -> SnackbarColors(
                background = Color(0xFF4C1D24),
                text = Color(0xFFFCA5A5),
                icon = Color(0xFFEF4444)
            )

            SnackbarType.WARNING -> SnackbarColors(
                background = Color(0xFF451A03),
                text = Color(0xFFFED7AA),
                icon = Color(0xFFF59E0B)
            )
        }
    } else {
        when (type) {
            SnackbarType.SUCCESS -> SnackbarColors(
                background = Color(0xFFDCFCE7),
                text = Color(0xFF166534),
                icon = Color(0xFF16A34A)
            )

            SnackbarType.ERROR -> SnackbarColors(
                background = Color(0xFFFEE2E2),
                text = Color(0xFF991B1B),
                icon = Color(0xFFDC2626)
            )

            SnackbarType.WARNING -> SnackbarColors(
                background = Color(0xFFFEF3C7),
                text = Color(0xFF92400E),
                icon = Color(0xFFD97706)
            )
        }
    }
}

private fun getSnackbarIcon(type: SnackbarType): ImageVector {
    return when (type) {
        SnackbarType.SUCCESS -> Icons.Default.CheckCircle
        SnackbarType.ERROR -> Icons.Default.Error
        SnackbarType.WARNING -> Icons.Default.Warning
    }
}
