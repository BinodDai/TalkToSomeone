package com.binod.talktosomeone.presentation.ui.screens.chat

import androidx.lifecycle.viewModelScope
import com.binod.talktosomeone.data.local.preferences.LocalStorage
import com.binod.talktosomeone.data.local.preferences.PrefKeys
import com.binod.talktosomeone.data.remote.api.FirestoreService
import com.binod.talktosomeone.di.DefaultStorage
import com.binod.talktosomeone.domain.aggregator.ChatUseCases
import com.binod.talktosomeone.domain.aggregator.ProfileUseCases
import com.binod.talktosomeone.domain.model.ChatMessage
import com.binod.talktosomeone.domain.model.MessageStatus
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.presentation.base.BaseViewModel
import com.binod.talktosomeone.utils.chatIdFor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCases,
    private val profileUseCases: ProfileUseCases,
    private val firestoreService: FirestoreService,
    @param:DefaultStorage private val defaultStorage: LocalStorage,
) : BaseViewModel() {

    val currentUserId: String? get() = firestoreService.currentUserId()
    private var isScreenActive = false

    private val _partnerProfile = MutableStateFlow<Profile?>(null)
    val partnerProfile: StateFlow<Profile?> = _partnerProfile

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _replyingTo = MutableStateFlow<ChatMessage?>(null)
    val replyingTo: StateFlow<ChatMessage?> = _replyingTo.asStateFlow()

    private val _isPartnerTyping = MutableStateFlow(false)
    val isPartnerTyping: StateFlow<Boolean> = _isPartnerTyping.asStateFlow()
    private var typingJob: Job? = null

    private val _currentEmoji = MutableStateFlow(
        defaultStorage[PrefKeys.DEFAULT_EMOJI, String::class.java].ifEmpty { "👋" }
    )
    val currentEmoji: StateFlow<String> = _currentEmoji.asStateFlow()


    fun start(chatPartnerId: String, myId: String) {
        val chatId = chatIdFor(myId, chatPartnerId)
        viewModelScope.launch {

            launch {
                chatUseCase.observeMessages(chatId).collect { list ->
                    _messages.value = list
                    chatUseCase.markDelivered(chatId, myId)

                    if (isScreenActive) {
                        markSeen(myId, chatPartnerId)
                    }
                }
            }

            launch {
                profileUseCases.observeProfile(chatPartnerId).collect { profile ->
                    _partnerProfile.value = profile
                    _isPartnerTyping.value = profile?.typingTo == myId
                }
            }
        }
    }

    fun onTextChanged(text: String, myId: String, partnerId: String) {
        typingJob?.cancel()

        if (text.isNotBlank()) {
            viewModelScope.launch {
                profileUseCases.setTypingTo(myId, partnerId)
            }

            typingJob = viewModelScope.launch {
                delay(3000)
                profileUseCases.setTypingTo(myId, null)
            }
        } else {
            viewModelScope.launch {
                profileUseCases.setTypingTo(myId, null)
            }
        }
    }


    fun sendEmoji(myId: String, partnerId: String) {
        clearTyping(myId)
        viewModelScope.launch {
            val msg = ChatMessage(
                senderId = myId,
                receiverId = partnerId,
                text = _currentEmoji.value,
                status = MessageStatus.SENT
            )
            chatUseCase.sendMessage(msg)
        }
    }

    fun clearTyping(myId: String) {
        typingJob?.cancel()
        viewModelScope.launch {
            profileUseCases.setTypingTo(myId, null)
        }
    }

    fun send(text: String, myId: String, partnerId: String, replyToMessageId: String?) {
        if (text.isBlank()) return
        clearTyping(myId)

        viewModelScope.launch {
            val msg = ChatMessage(
                senderId = myId,
                receiverId = partnerId,
                text = text,
                status = MessageStatus.SENT,
                replyToMessageId = replyToMessageId
            )
            chatUseCase.sendMessage(msg)
        }
    }

    fun sendReply(text: String, myId: String, partnerId: String, replyToMessageId: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val msg = ChatMessage(
                senderId = myId,
                receiverId = partnerId,
                text = text,
                status = MessageStatus.SENT,
                replyToMessageId = replyToMessageId
            )
            chatUseCase.sendMessage(msg)
            _replyingTo.value = null
        }
    }

    fun addReaction(myId: String, partnerId: String, messageId: String, emoji: String) {
        val chatId = chatIdFor(myId, partnerId)
        viewModelScope.launch {
            chatUseCase.addReaction(chatId, messageId, myId, emoji)
        }
    }

    fun markSeen(myId: String, partnerId: String) {
        val chatId = chatIdFor(myId, partnerId)
        viewModelScope.launch {
            chatUseCase.markSeen(chatId, myId)
        }
    }

    fun onScreenActive() {
        isScreenActive = true
        currentUserId?.let { myId ->
            _partnerProfile.value?.userId?.let { partnerId ->
                markSeen(myId, partnerId)
            }
        }
    }

    fun onScreenInactive() {
        isScreenActive = false
    }

    fun setReplyingTo(message: ChatMessage?) {
        _replyingTo.value = message
    }


    fun getMessageById(chatId: String, messageId: String): StateFlow<ChatMessage?> {
        val messageFlow = MutableStateFlow<ChatMessage?>(null)
        viewModelScope.launch {
            messageFlow.value = chatUseCase.getMessageById(chatId, messageId)
        }
        return messageFlow.asStateFlow()
    }

    fun getPartnerProfileById(partnerId: String) {
        viewModelScope.launch {
            setLoading(true)
            try {
                val profile = profileUseCases.getProfile(userId = partnerId)
                _partnerProfile.value = profile
            } catch (e: Exception) {
                showErrorSnackbar("Failed to load a profile. Please try again.")
            } finally {
                setLoading(false)
            }
        }
    }

    fun updateDefaultEmoji(emoji: String) {
        defaultStorage.set(PrefKeys.DEFAULT_EMOJI, emoji)
        _currentEmoji.value = emoji
    }
}


