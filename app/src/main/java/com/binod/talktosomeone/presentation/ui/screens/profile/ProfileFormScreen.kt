package com.binod.talktosomeone.presentation.ui.screens.profile

import AppButton
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.binod.talktosomeone.R
import com.binod.talktosomeone.presentation.navigation.Screen
import com.binod.talktosomeone.presentation.ui.components.common.AppOutlinedTextField
import com.binod.talktosomeone.presentation.ui.components.common.CustomSnackbar
import com.binod.talktosomeone.presentation.ui.components.common.FullScreenLoader
import com.binod.talktosomeone.presentation.ui.components.common.HandleUiEvents
import com.binod.talktosomeone.presentation.ui.components.common.ItemListBottomSheet
import com.binod.talktosomeone.presentation.ui.theme.dimensions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileFormScreen(
    navController: NavController,
    viewModel: ProfileFormViewModel = hiltViewModel()
) {
    val aiNameCode by viewModel.aiNameCode.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val age by viewModel.age.collectAsState()
    val selectedGender by viewModel.gender.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showGenderSheet by remember { mutableStateOf(false) }

    val genderOptions = listOf("Male", "Female", "Other", "Prefer not to say")
    val coroutineScope = rememberCoroutineScope()

    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarState by viewModel.snackbarState.collectAsState()

    HandleUiEvents(
        uiEvent = viewModel.uiEvent,
        navController = navController,
        popUpToRoute = Screen.Profile.route,
        onShowToast = { viewModel.showSnackbar(it) }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(dimensions.paddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = stringResource(R.string.profile_icon),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(dimensions.spaceSmall))
                Text(
                    text = stringResource(R.string.create_profile),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Text(
                text = stringResource(R.string.design_your_unique_identity),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = dimensions.paddingSmall)
            )

            Spacer(Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation)
            ) {
                Column(
                    modifier = Modifier.padding(dimensions.paddingLarge),
                    verticalArrangement = Arrangement.spacedBy(dimensions.spaceLarge)
                ) {
                    // Name
                    Column {
                        Text(
                            text = stringResource(R.string.code_name),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(dimensions.spaceSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppOutlinedTextField(
                                value = aiNameCode,
                                onValueChange = { viewModel.updateNameCode(it) },
                                modifier = Modifier.weight(1f),
                                placeholder = stringResource(R.string.enter_or_shuffle_name)
                            )

                            Button(
                                onClick = { viewModel.generateAiName() },
                                enabled = !isGenerating,
                                modifier = Modifier.size(56.dp),
                                shape = RoundedCornerShape(dimensions.radiusMedium),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                if (isGenerating) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Age
                    Column {
                        Text(
                            text = stringResource(R.string.age),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        AppOutlinedTextField(
                            value = age,
                            onValueChange = { viewModel.updateAge(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = stringResource(R.string.enter_your_age),
                            onlyDigits = true,
                            maxLength = 3,
                            keyboardType = KeyboardType.Number
                        )
                    }

                    // Gender
                    Column {
                        Text(
                            text = stringResource(R.string.gender),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showGenderSheet = true
                                    coroutineScope.launch { sheetState.show() }
                                }
                        ) {
                            AppOutlinedTextField(
                                value = selectedGender,
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = stringResource(R.string.select_gender),
                                readOnly = true,
                                enabled = false,
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(dimensions.spaceLarge))

                    // Create Profile Button
                    AppButton(
                        text = stringResource(R.string.create_profile),
                        icon = Icons.Default.RocketLaunch,
                        enabled = aiNameCode.isNotBlank() && age.isNotBlank() && selectedGender.isNotBlank(),
                        onClick = { viewModel.createProfile() },
                        height = dimensions.buttonHeightLarge,
                        shape = RoundedCornerShape(dimensions.radiusMedium)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = buildAnnotatedString {
                    append("Chat anonymously in a safe space with your code name ")
                    if (aiNameCode.isNotBlank()) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                            append(aiNameCode)
                        }
                    }
                    append(". Your identity remains private.")
                },
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensions.paddingMedium)
            )
        }

        if (snackbarState.isVisible) {
            CustomSnackbar(
                snackbarState = snackbarState,
                onDismiss = { viewModel.dismissSnackbar() },
                modifier = Modifier.statusBarsPadding()
            )
        }

        if (isLoading) FullScreenLoader()
    }

    // Gender Bottom Sheet
    if (showGenderSheet) {
        ItemListBottomSheet(
            sheetState = sheetState,
            onDismiss = {
                coroutineScope.launch { sheetState.hide() }
                    .invokeOnCompletion { showGenderSheet = false }
            },
            listOptions = genderOptions,
            selectedItem = selectedGender,
            onItemSelected = { viewModel.updateGender(it) },
            title = stringResource(R.string.select_gender)
        )
    }
}

