package com.binod.talktosomeone.presentation.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.binod.talktosomeone.domain.aggregator.HomeUseCases
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.presentation.base.BaseViewModel
import com.binod.talktosomeone.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(private val homeUseCases: HomeUseCases) : BaseViewModel() {

    private val _matchedProfile = MutableStateFlow<Profile?>(null)
    val matchedProfile: StateFlow<Profile?> = _matchedProfile


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