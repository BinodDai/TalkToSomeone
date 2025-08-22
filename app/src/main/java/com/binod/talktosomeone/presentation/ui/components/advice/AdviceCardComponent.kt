package com.binod.talktosomeone.presentation.ui.components.advice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.binod.talktosomeone.domain.model.AdviceCard
import com.binod.talktosomeone.presentation.ui.theme.Gray200
import com.binod.talktosomeone.presentation.ui.theme.Gray400
import com.binod.talktosomeone.presentation.ui.theme.Gray500
import com.binod.talktosomeone.presentation.ui.theme.Shapes
import com.binod.talktosomeone.presentation.ui.theme.dimensions
import com.binod.talktosomeone.utils.isLightTheme

@Composable
fun AdviceCardComponent(advice: AdviceCard) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 0.5.dp,
                color = Gray200,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { }
            .padding(dimensions.paddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        advice.backgroundColor,
                        shape = Shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = advice.icon,
                    contentDescription = advice.title,
                    tint = advice.iconTintColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(dimensions.paddingMedium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = advice.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(dimensions.paddingSmall))
            Text(
                text = advice.des,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isLightTheme()) Gray500 else Gray400
            )
        }
    }
}