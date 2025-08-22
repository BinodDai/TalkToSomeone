package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.repository.ProfileRepository
import javax.inject.Inject

class SetUserPresenceUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend fun setOnline() {
        repository.setOnline(true)
    }

    suspend fun setOffline() {
        repository.setOnline(false)
    }
}
