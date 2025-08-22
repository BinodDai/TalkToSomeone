package com.binod.talktosomeone.presentation.ui.components.advice

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
import com.binod.talktosomeone.domain.model.AdviceCard
import com.binod.talktosomeone.presentation.ui.theme.dimensions

@Composable
fun AdviceCardComponentSection(adviceCard: List<AdviceCard>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = stringResource(R.string.what_area_do_you_need_help_with),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(dimensions.marginMedium))

        Column(verticalArrangement = Arrangement.spacedBy(dimensions.marginSmall)) {
            adviceCard.forEach { advice ->
                AdviceCardComponent(advice = advice)
            }
        }
    }
}