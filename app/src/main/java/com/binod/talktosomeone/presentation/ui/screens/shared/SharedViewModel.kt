package com.binod.talktosomeone.presentation.ui.screens.shared

import androidx.lifecycle.ViewModel
import com.binod.talktosomeone.domain.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _selectedProfile = MutableStateFlow<Profile?>(null)
    val selectedProfile: StateFlow<Profile?> = _selectedProfile.asStateFlow()

    fun setProfile(profile: Profile) {
        _selectedProfile.value = profile
    }

    fun clearProfile() {
        _selectedProfile.value = null
    }
}