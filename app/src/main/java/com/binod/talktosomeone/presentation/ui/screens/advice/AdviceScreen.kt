package com.binod.talktosomeone.presentation.ui.screens.advice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.binod.talktosomeone.R
import com.binod.talktosomeone.domain.model.AdviceCard
import com.binod.talktosomeone.presentation.ui.components.advice.AdviceCardComponentSection
import com.binod.talktosomeone.presentation.ui.components.common.IconTitleDescription
import com.binod.talktosomeone.presentation.ui.theme.AdviceColor
import com.binod.talktosomeone.presentation.ui.theme.Gray50
import com.binod.talktosomeone.presentation.ui.theme.VentColor
import com.binod.talktosomeone.presentation.ui.theme.dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdviceScreen(navController: NavController) {

    val adviceCards = listOf(
        AdviceCard(
            title = "Stay Positive",
            des = "Always look for the bright side in every situation.",
            icon = Icons.Default.Info,
            iconTintColor = VentColor,
            backgroundColor = AdviceColor
        ),


        AdviceCard(
            title = "Stay Positive",
            des = "Always look for the bright side in every situation.",
            icon = Icons.Default.Info,
            iconTintColor = VentColor,
            backgroundColor = AdviceColor
        ),

        AdviceCard(
            title = "Stay Positive",
            des = "Always look for the bright side in every situation.",
            icon = Icons.Default.Info,
            iconTintColor = VentColor,
            backgroundColor = AdviceColor
        ),
    )

    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.padding(vertical = dimensions.paddingMedium),
            title = {
                Text(
                    text = stringResource(R.string.advice),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
    }) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = dimensions.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(dimensions.paddingLarge)
        ) {
            item {
                IconTitleDescription(
                    icon = Icons.Default.Lightbulb,
                    iconTintColor = Gray50,
                    title = stringResource(R.string.advice),
                    description = stringResource(R.string.connect_with_someone_who_can_offer_perspective_and_practical_guidance_on_your_situation)
                )
            }

            item {
                AdviceCardComponentSection(adviceCards)
            }
        }
    }
}