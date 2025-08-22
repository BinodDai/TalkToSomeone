package com.binod.talktosomeone.presentation.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.binod.talktosomeone.domain.model.StatCard
import com.binod.talktosomeone.presentation.ui.theme.dimensions

@Composable
fun StatsSection(stats: List<StatCard>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensions.paddingMedium)
    ) {
        stats.forEach { stat ->
            StatCardComponent(
                stat = stat,
                modifier = Modifier.weight(1f)
            )
        }
    }
}