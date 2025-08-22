package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): Profile? {
        return repository.getProfile(userId)
    }
}
