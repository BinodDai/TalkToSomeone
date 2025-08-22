package com.binod.talktosomeone.presentation.ui.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.binod.talktosomeone.R
import com.binod.talktosomeone.presentation.ui.theme.dimensions


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    listOptions: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    title: String = stringResource(R.string.select_option)
) {
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

            listOptions.forEach { option ->
                val isSelected = option == selectedItem

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemSelected(option)
                            onDismiss()
                        }
                        .padding(vertical = dimensions.padding12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option,
                        modifier = Modifier.weight(1f),
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        style = if (isSelected) {
                            MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            MaterialTheme.typography.bodyLarge
                        }
                    )

                    if (isSelected) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(dimensions.paddingLarge))
        }
    }
}