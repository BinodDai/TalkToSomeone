package com.binod.talktosomeone.presentation.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.binod.talktosomeone.domain.model.StatCard
import com.binod.talktosomeone.presentation.ui.theme.SurfaceDark
import com.binod.talktosomeone.presentation.ui.theme.dimensions
import com.binod.talktosomeone.utils.isLightTheme

@Composable
fun StatCardComponent(
    stat: StatCard,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(
                if (onClick != null) Modifier.clickable { onClick() }
                else Modifier
            ),
        shape = RoundedCornerShape(dimensions.spaceMedium),
        colors = CardDefaults.cardColors(
            containerColor = if (isLightTheme()) stat.color else SurfaceDark
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.default)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensions.paddingMedium),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stat.value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stat.label,
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = dimensions.paddingExtraSmall)
            )
        }
    }
}

