package com.binod.talktosomeone.presentation.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binod.talktosomeone.R
import com.binod.talktosomeone.presentation.ui.theme.dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    selectedEmoji: String,
    onEmojiSelected: (String) -> Unit,
    title: String = stringResource(R.string.select_emoji)
) {
    val emojiList = listOf(
        // 😀 Faces
        "😀", "😁", "😂", "🤣", "😅", "😊", "😇", "🙂", "🙃",
        "😉", "😍", "😘", "😋", "😜", "🤪", "😎", "🥳", "🤩",
        "😏", "😢", "😭", "😡", "😤", "😱", "😨", "😴", "🥺", "🤔",

        // 🙌 Gestures
        "👍", "👎", "👌", "✌️", "🤞", "🤟", "🤘", "👏", "🙌",
        "🤝", "🙏", "🫶", "👋", "🤙",

        // ❤️ Love / emotions
        "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍",
        "💔", "💕", "💞", "💓", "💗", "💖", "💘", "💝",
        "💋", "🌹",

        // 🔥 Fun / hype
        "🔥", "💯", "✨", "🎉", "🎊", "⭐", "🌟", "⚡",
        "👑", "🥂", "🍻", "🍕", "🍫", "🍦",

        // 🙈 Playful / anonymous vibe
        "🙈", "🙉", "🙊", "👻", "😈", "👽", "🤡", "🥸", "😷",
        "💀", "☠️", "🤖", "🦄", "🐼", "🐧"
    )


    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.paddingLarge)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(dimensions.spaceMedium))

            // Emoji Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                horizontalArrangement = Arrangement.spacedBy(dimensions.spaceSmall),
                verticalArrangement = Arrangement.spacedBy(dimensions.spaceSmall),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(emojiList) { emoji ->
                    val isSelected = emoji == selectedEmoji

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else Color.Transparent
                            )
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                onEmojiSelected(emoji)
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 24.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(dimensions.paddingLarge))
        }
    }
}