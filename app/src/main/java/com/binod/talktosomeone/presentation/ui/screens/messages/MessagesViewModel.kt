package com.binod.talktosomeone.presentation.ui.screens.messages

import android.text.format.DateUtils
import androidx.lifecycle.viewModelScope
import com.binod.talktosomeone.data.remote.api.FirestoreService
import com.binod.talktosomeone.domain.aggregator.ChatUseCases
import com.binod.talktosomeone.domain.aggregator.ProfileUseCases
import com.binod.talktosomeone.domain.model.RecentChat
import com.binod.talktosomeone.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val chatUseCases: ChatUseCases,
    private val firestoreService: FirestoreService,
) : BaseViewModel() {
    private val _allChats = MutableStateFlow<List<RecentChat>>(emptyList())
    val allChats: StateFlow<List<RecentChat>> = _allChats

    val currentUserId = firestoreService.currentUserId()

    init {
        observeAllChats()
    }

    fun observeAllChats() {
        val myId = currentUserId ?: return
        viewModelScope.launch {
            chatUseCases.observeAllChatsUseCase().collect { chats ->
                val mapped = chats.mapNotNull { summary ->
                    val otherUserId =
                        summary.participants.firstOrNull { it != myId } ?: return@mapNotNull null
                    val profile = profileUseCases.getProfile(otherUserId)

                    profile?.let {
                        RecentChat(
                            chatId = summary.chatId,
                            userId = otherUserId,
                            name = it.codeName,
                            timeAgo = DateUtils.getRelativeTimeSpanString(summary.lastTimestamp)
                                .toString(),
                            online = it.online,
                            avatarText = it.codeName.take(1).uppercase(),
                            lastMessage = summary.lastMessage,
                        )
                    }
                }
                _allChats.value = mapped
            }
        }
    }

}