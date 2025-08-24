package com.binod.talktosomeone.presentation.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.binod.talktosomeone.presentation.ui.components.chat.AnimatedTypingIndicator
import com.binod.talktosomeone.presentation.ui.components.chat.MessageItem
import com.binod.talktosomeone.presentation.ui.components.chat.ReplyPreviewBar
import com.binod.talktosomeone.presentation.ui.components.common.EmojiBottomSheet
import com.binod.talktosomeone.presentation.ui.theme.Shapes
import com.binod.talktosomeone.presentation.ui.theme.dimensions
import com.binod.talktosomeone.utils.formatLastSeen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatPartnerId: String,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val currentUserId = viewModel.currentUserId ?: ""
    val messages by viewModel.messages.collectAsState()
    val partnerProfile by viewModel.partnerProfile.collectAsState()
    val isPartnerTyping by viewModel.isPartnerTyping.collectAsState()
    val replyingTo by viewModel.replyingTo.collectAsState()
    val currentEmoji by viewModel.currentEmoji.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showEmojiSheet by remember { mutableStateOf(false) }

    var messageText by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(chatPartnerId) {
        coroutineScope {
            launch { viewModel.getPartnerProfileById(chatPartnerId) }
            launch { viewModel.start(chatPartnerId, currentUserId) }
        }
    }

    DisposableEffect(Unit) {
        viewModel.onScreenActive()
        onDispose {
            viewModel.onScreenInactive()
            viewModel.clearTyping(currentUserId)
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            viewModel.markSeen(currentUserId, chatPartnerId)
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val displayInitialChar = partnerProfile?.codeName?.firstOrNull()?.toString() ?: ""


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(dimensions.avatarMedium)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = displayInitialChar,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(dimensions.spaceSmall))
                        Column {
                            Text(
                                text = partnerProfile?.codeName
                                    ?: stringResource(com.binod.talktosomeone.R.string.loading),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = when {
                                    isPartnerTyping -> "typing..."
                                    partnerProfile?.online == true -> "Online"
                                    partnerProfile?.lastSeen != null -> {
                                        val lastSeenTime = Date(partnerProfile!!.lastSeen!!)
                                        "Last seen ${formatLastSeen(lastSeenTime)}"
                                    }

                                    else -> "Offline"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isPartnerTyping) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            navController.navigateUp()
                        }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
//                actions = {
//                    IconButton(onClick = { /* More options */ }) {
//                        Icon(
//                            imageVector = Icons.Default.MoreVert,
//                            contentDescription = "More",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
//                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji button with long press functionality
                    Box(
                        modifier = Modifier
                            .size(dimensions.iconExtraLarge)
                            .combinedClickable(
                                onClick = {
                                    // Single click - send emoji as message
                                    viewModel.sendEmoji(currentUserId, chatPartnerId)
                                    scope.launch {
                                        if (messages.isNotEmpty()) {
                                            listState.animateScrollToItem(messages.size)
                                        }
                                    }
                                },
                                onLongClick = {
                                    showEmojiSheet = true
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentEmoji,
                            fontSize = dimensions.iconLarge.value.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(dimensions.paddingExtraSmall))

                    // Text input
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { newText ->
                            messageText = newText
                            viewModel.onTextChanged(newText, currentUserId, chatPartnerId)
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = "Type a message...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        shape = Shapes.extraLarge,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.width(dimensions.spaceSmall))

                    // Send/Record button
                    IconButton(
                        onClick = {
                            if (messageText.isBlank()) {
                                isRecording = !isRecording
                            } else {
                                viewModel.send(
                                    messageText,
                                    currentUserId,
                                    chatPartnerId,
                                    replyingTo?.id
                                )
                                messageText = ""
                                viewModel.setReplyingTo(null)
                                scope.launch {
                                    if (messages.isNotEmpty()) {
                                        listState.animateScrollToItem(messages.size - 1)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .size(dimensions.iconExtraLarge)
                            .clip(CircleShape)
                            .background(
                                if (messageText.isBlank() && isRecording) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.primary
                            )
                    ) {
                        Icon(
                            imageVector = if (messageText.isBlank()) Icons.Default.Mic else Icons.AutoMirrored.Filled.Send,
                            contentDescription = if (messageText.isBlank()) "Record" else "Send",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Messages List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(dimensions.spaceSmall),
                contentPadding = PaddingValues(vertical = dimensions.paddingSmall)
            ) {
                items(messages) { message ->
                    val repliedMessage = message.replyToMessageId?.let { replyId ->
                        messages.find { it.id == replyId }
                    }

                    MessageItem(
                        message = message,
                        currentUserId = currentUserId,
                        partnerProfile = partnerProfile,
                        repliedMessage = repliedMessage,
                        onReplySwipe = { msg ->
                            viewModel.setReplyingTo(msg)
                        }
                    )
                }
            }

            // Typing indicator
            AnimatedTypingIndicator(isVisible = isPartnerTyping, userName = displayInitialChar)

            if (replyingTo != null) {
                ReplyPreviewBar(
                    replyingTo = replyingTo!!,
                    currentUserId = currentUserId,
                    partnerProfile = partnerProfile,
                    onCancelReply = { viewModel.setReplyingTo(null) },
                    modifier = Modifier.padding(horizontal = dimensions.paddingMedium)
                )
            }
        }
    }

    if (showEmojiSheet) {
        EmojiBottomSheet(
            sheetState = sheetState,
            onDismiss = { showEmojiSheet = false },
            selectedEmoji = currentEmoji,
            onEmojiSelected = { selectedEmoji ->
                viewModel.updateDefaultEmoji(selectedEmoji)
                showEmojiSheet = false
            }
        )
    }
}

