package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.repository.ProfileRepository
import javax.inject.Inject

class SignInAnonymouslyUseCase @Inject constructor
    (private val repository: ProfileRepository) {
    suspend operator fun invoke(): String = repository.signInAnonymously()
}
