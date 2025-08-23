package com.binod.talktosomeone.presentation.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.binod.talktosomeone.R
import com.binod.talktosomeone.domain.model.ConversationStarter
import com.binod.talktosomeone.domain.model.ConversationStarterType
import com.binod.talktosomeone.domain.model.StatCard
import com.binod.talktosomeone.presentation.navigation.Screen
import com.binod.talktosomeone.presentation.ui.components.common.CustomSnackbar
import com.binod.talktosomeone.presentation.ui.components.common.FullScreenLoader
import com.binod.talktosomeone.presentation.ui.components.common.HandleUiEvents
import com.binod.talktosomeone.presentation.ui.components.common.IconCircleButton
import com.binod.talktosomeone.presentation.ui.components.home.ConversationStartersSection
import com.binod.talktosomeone.presentation.ui.components.home.GreetingSection
import com.binod.talktosomeone.presentation.ui.components.home.RecentConversationsSection
import com.binod.talktosomeone.presentation.ui.components.home.StatsSection
import com.binod.talktosomeone.presentation.ui.theme.Gray100
import com.binod.talktosomeone.presentation.ui.theme.Gray50
import com.binod.talktosomeone.presentation.ui.theme.dimensions
import com.binod.talktosomeone.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val matchedProfile by viewModel.matchedProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val snackbarState by viewModel.snackbarState.collectAsState()
    var selectedType by remember { mutableStateOf<ConversationStarterType?>(null) }

    val onlineCount by viewModel.onlineCount.collectAsState()
    val todayChats by viewModel.todayChats.collectAsState()
    val recentChats by viewModel.recentChats.collectAsState()
    val currentUserId = viewModel.currentUserId ?: ""

    LaunchedEffect(Unit) {
        viewModel.loadStats()
        viewModel.loadRecentChats()
    }


    HandleUiEvents(
        uiEvent = viewModel.uiEvent,
        navController = navController,
        popUpToRoute = Screen.Chat.route,
        onShowToast = { message ->
            viewModel.showSnackbar(message)
        }
    )

    val stats = listOf(
        StatCard("$onlineCount", "People Online", Gray100),
        StatCard("${todayChats.distinctBy { it.lastSenderId }.size}", "Your chats today", Gray100)
    )

    val conversationStarters = listOf(
        ConversationStarter(
            Constants.QUICK_MATCH,
            stringResource(R.string.connect_instantly),
            Icons.Default.Bolt,
            iconTint = Gray50,
            type = ConversationStarterType.QUICK_MATCH
        ),

        ConversationStarter(
            Constants.VENT,
            stringResource(R.string.just_need_to_talk),
            Icons.Default.Cloud,
            iconTint = Gray50,
            type = ConversationStarterType.VENT
        ),

        ConversationStarter(
            Constants.ADVICE,
            stringResource(R.string.get_perspective),
            Icons.Default.Lightbulb,
            iconTint = Gray50,
            type = ConversationStarterType.ADVICE
        ),

        ConversationStarter(
            Constants.TOPIC,
            stringResource(R.string.choose_interest),
            Icons.Default.TrackChanges,
            iconTint = Gray50,
            type = ConversationStarterType.TOPIC
        )

    )

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(
                    horizontal = dimensions.paddingSmall,
                    vertical = dimensions.paddingSmall
                ),
                title = {
                    Text("John Doe", style = MaterialTheme.typography.titleLarge)
                },
                navigationIcon = {
                    IconCircleButton(
                        icon = { Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy") },
                        onClick = { /* copy */ }
                    )
                },
                actions = {
                    IconCircleButton(
                        icon = { Icon(Icons.Outlined.Search, contentDescription = "Search") },
                        onClick = { /* search */ }
                    )
                    Spacer(Modifier.padding(dimensions.paddingExtraSmall))
                    IconCircleButton(
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Outlined.Message,
                                contentDescription = "Message"
                            )
                        },
                        onClick = { /* message */ }
                    )
                }
            )
        }
    )


    { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = dimensions.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(dimensions.paddingLarge)
        ) {
            item { GreetingSection() }
            item { StatsSection(stats = stats) }
            item {
                ConversationStartersSection(
                    starters = conversationStarters,
                    selectedType = selectedType,
                    onItemClick = { type ->
                        selectedType = type

                        when (type) {
                            ConversationStarterType.QUICK_MATCH -> {
                                viewModel.quickMatch()
                            }

                            ConversationStarterType.VENT -> {

                            }

                            ConversationStarterType.ADVICE -> {

                            }

                            ConversationStarterType.TOPIC -> {

                            }
                        }

                    })
            }

            if (recentChats.isNotEmpty()) {
                item {
                    RecentConversationsSection(
                        recentChats = recentChats,
                        onItemClick = { chat ->
                            navController.navigate(Screen.Chat.withArgs(chat.userId))
                        }
                    )
                }
            }
        }
    }
    if (snackbarState.isVisible) {
        CustomSnackbar(
            snackbarState = snackbarState,
            onDismiss = { viewModel.dismissSnackbar() },
            modifier = Modifier
                .statusBarsPadding()
        )
    }

    if (isLoading) {
        FullScreenLoader()
    }
}

