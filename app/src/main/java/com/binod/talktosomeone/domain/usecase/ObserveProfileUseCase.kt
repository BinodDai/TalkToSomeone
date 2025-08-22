package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(userId: String): Flow<Profile?> {
        return repository.observeProfile(userId)
    }
}
