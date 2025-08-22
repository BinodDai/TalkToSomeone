package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.repository.ProfileRepository
import javax.inject.Inject

class CreateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile) {
        repository.createProfile(profile)
    }
}
