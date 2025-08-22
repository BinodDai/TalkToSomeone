package com.binod.talktosomeone.presentation.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FullScreenLoader(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
    indicatorColor: Color = Color.White,
    size: Int = 60
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = indicatorColor,
            strokeWidth = 4.dp,
            modifier = Modifier.size(size.dp)
        )
    }
}
