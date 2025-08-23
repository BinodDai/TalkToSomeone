package com.binod.talktosomeone.presentation.ui.screens.home

import android.text.format.DateUtils
import androidx.lifecycle.viewModelScope
import com.binod.talktosomeone.data.local.preferences.LocalStorage
import com.binod.talktosomeone.data.local.preferences.PrefKeys
import com.binod.talktosomeone.data.remote.api.FirestoreService
import com.binod.talktosomeone.di.SecureStorage
import com.binod.talktosomeone.domain.aggregator.HomeUseCases
import com.binod.talktosomeone.domain.aggregator.ProfileUseCases
import com.binod.talktosomeone.domain.model.ChatSummary
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.model.RecentChat
import com.binod.talktosomeone.presentation.base.BaseViewModel
import com.binod.talktosomeone.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val profileUseCases: ProfileUseCases,
    private val firestoreService: FirestoreService,
    @param:SecureStorage private val securedStorage: LocalStorage,
) : BaseViewModel() {

    val currentUserId: String? get() = firestoreService.currentUserId()
    val nameCode: String = securedStorage[PrefKeys.CODE_NAME, String::class.java]

    private val _matchedProfile = MutableStateFlow<Profile?>(null)
    val matchedProfile: StateFlow<Profile?> = _matchedProfile

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    private val _onlineCount = MutableStateFlow(0)
    val onlineCount: StateFlow<Int> = _onlineCount

    private val _todayChats = MutableStateFlow<List<ChatSummary>>(emptyList())
    val todayChats: StateFlow<List<ChatSummary>> = _todayChats

    private val _recentChats = MutableStateFlow<List<RecentChat>>(emptyList())
    val recentChats: StateFlow<List<RecentChat>> = _recentChats

    fun loadStats() {
        viewModelScope.launch {
            try {
                _onlineCount.value = homeUseCases.getOnlinePeopleCount()
            } catch (e: Exception) {
                showErrorSnackbar("Failed to load stats")
            }
        }
    }

    suspend fun getProfileByUserId(userID: String): Profile? {
        return try {
            val profile = profileUseCases.getProfile(userID)
            _profile.value = profile
            profile
        } catch (e: Exception) {
            null
        }
    }

    fun loadRecentChats() {
        viewModelScope.launch {
            val chats = homeUseCases.getTodayChats()
            val myId = currentUserId ?: return@launch

            val recentChats = chats.mapNotNull { summary ->
                val otherUserId = if (summary.userA == myId) summary.userB else summary.userA
                val profile = profileUseCases.getProfile(otherUserId)

                profile?.let {
                    RecentChat(
                        userId = otherUserId,
                        chatId = summary.chatId,
                        name = it.codeName,
                        timeAgo = DateUtils.getRelativeTimeSpanString(summary.lastTimestamp)
                            .toString(),
                        online = it.online,
                        avatarText = it.codeName.take(1).uppercase(),
                        lastMessage = summary.lastMessage
                    )
                }
            }

            _todayChats.value = chats
            _recentChats.value = recentChats
        }
    }

    fun quickMatch(currentUserAge: Int? = null, gender: String? = null) {
        viewModelScope.launch {
            setLoading(true)
            try {
                val profile = homeUseCases.findQuickMatch(currentUserAge, gender)
                _matchedProfile.value = profile
                if (profile != null) {
                    sendEvent(UiEvent.Navigate(Screen.Chat.withArgs(profile.userId)))
                } else {
                    showWarningSnackbar("No matches found at the moment. Please try again later.")
                }
            } catch (e: Exception) {
                showErrorSnackbar("Failed to find a match. Please try again.")
            } finally {
                setLoading(false)
            }
        }
    }
}