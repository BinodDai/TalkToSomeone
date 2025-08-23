package com.binod.talktosomeone.presentation.ui.screens.messages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.binod.talktosomeone.presentation.navigation.Screen
import com.binod.talktosomeone.presentation.ui.components.common.EmptyStateScreen
import com.binod.talktosomeone.presentation.ui.components.home.RecentChatItem
import com.binod.talktosomeone.presentation.ui.theme.dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    navController: NavController,
    viewModel: MessagesViewModel = hiltViewModel(),
) {

    val allChats by viewModel.allChats.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Messages",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
    ) { innerPadding ->

        if (allChats.isEmpty()) {
            EmptyStateScreen(message = "No messages found")
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = dimensions.paddingMedium,
                        vertical = dimensions.paddingSmall
                    )
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(dimensions.marginSmall)
            ) {
                allChats.distinctBy {
                    it.userId
                }.forEach { chat ->
                    RecentChatItem(chat = chat, onItemClick = {
                        navController.navigate(Screen.Chat.withArgs(chat.userId))
                    })
                }
            }
        }

    }
}