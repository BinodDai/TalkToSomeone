package com.binod.talktosomeone.presentation.ui.components.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.binod.talktosomeone.R
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.presentation.ui.theme.Shapes
import com.binod.talktosomeone.presentation.ui.theme.dimensions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUserBottomSheet(
    sheetState: SheetState,
    currentUserId: String,
    onDismiss: () -> Unit,
    onSearch: suspend (String) -> Profile?,
    onContinue: (Profile) -> Unit,
    title: String = stringResource(R.string.search_user)
) {
    var userId by remember { mutableStateOf("") }
    var profile by remember { mutableStateOf<Profile?>(null) }
    var searchAttempted by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensions.paddingLarge,
                    end = dimensions.paddingLarge,
                    bottom = dimensions.paddingLarge
                )
        ) {
            // Title
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(dimensions.spaceMedium))

            // UserId input field
            OutlinedTextField(
                value = userId,
                onValueChange = { newValue ->
                    if (newValue.length <= 50) userId = newValue
                    profile = null
                    searchAttempted = false

                    // Auto-search if it looks like a Firestore UID
                    if (newValue.length in 28..36 && newValue.all { it.isLetterOrDigit() }) {
                        scope.launch {
                            isSearching = true
                            profile = onSearch(newValue)
                            searchAttempted = true
                            isSearching = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.user_id)) },
                shape = Shapes.medium,
                singleLine = true,
                trailingIcon = {
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        IconButton(onClick = {
                            if (userId.isNotBlank()) {
                                scope.launch {
                                    isSearching = true
                                    profile = null
                                    profile = onSearch(userId)
                                    searchAttempted = true
                                    isSearching = false
                                }
                            }
                        }) {
                            Icon(Icons.Outlined.Search, contentDescription = "Search")
                        }
                    }
                }
            )


            Spacer(Modifier.height(dimensions.spaceMedium))

            AnimatedVisibility(visible = currentUserId == profile?.userId) {
                InfoCard(
                    text = "You cannot search for yourself. Please enter another User ID.",
                    backgroundColor = MaterialTheme.colorScheme.errorContainer,
                    textColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            AnimatedVisibility(visible = searchAttempted && profile != null && currentUserId != profile?.userId) {
                InfoCard(
                    text = "User found:\n ${profile?.codeName ?: ""}",
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    textColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = searchAttempted && profile == null && !isSearching) {
                InfoCard(
                    text = "User not found.\nPlease check the User ID and try again.",
                    backgroundColor = MaterialTheme.colorScheme.errorContainer,
                    textColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            Spacer(Modifier.height(dimensions.paddingLarge))

            // Cancel & Continue buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensions.spaceMedium),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = profile != null && profile?.userId != currentUserId,
                    onClick = {
                        profile?.let { it -> onContinue(it) }
                        onDismiss()
                    }
                ) {
                    Text(stringResource(R.string.continue_label))
                }
            }
        }
    }
}