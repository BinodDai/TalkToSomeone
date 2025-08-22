package com.binod.talktosomeone.domain.repository

import com.binod.talktosomeone.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profileFlow: Flow<Profile?>
    suspend fun createProfile(profile: Profile)
    suspend fun refreshProfile(userId: String): Profile?
    suspend fun getProfile(userId: String): Profile?
    suspend fun signInAnonymously(): String
    suspend fun setTypingTo(userId: String, typingTo: String?)
    suspend fun setOnline(isOnline: Boolean)
    fun observeProfile(userId: String): Flow<Profile?>
}
