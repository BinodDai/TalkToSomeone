package com.binod.talktosomeone.presentation.ui.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.binod.talktosomeone.presentation.ui.theme.dimensions
import com.binod.talktosomeone.utils.getGreetingWithEmoji

@Composable
fun GreetingSection() {
    Column {
        Text(
            text = getGreetingWithEmoji(),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(dimensions.paddingSmall))

        Text(
            text = stringResource(com.binod.talktosomeone.R.string.how_are_you_feeling_today_ready_to_connect),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}