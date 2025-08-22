package com.binod.talktosomeone.domain.aggregator

import com.binod.talktosomeone.domain.usecase.CreateProfileUseCase
import com.binod.talktosomeone.domain.usecase.GetProfileUseCase
import com.binod.talktosomeone.domain.usecase.ObserveProfileUseCase
import com.binod.talktosomeone.domain.usecase.RefreshProfileUseCase
import com.binod.talktosomeone.domain.usecase.SetTypingToUseCase
import com.binod.talktosomeone.domain.usecase.SetUserPresenceUseCase
import com.binod.talktosomeone.domain.usecase.SignInAnonymouslyUseCase
import javax.inject.Inject

class ProfileUseCases @Inject constructor(
    val createProfile: CreateProfileUseCase,
    val getProfile: GetProfileUseCase,
    val refreshProfile: RefreshProfileUseCase,
    val signInAnonymously: SignInAnonymouslyUseCase,
    val setTypingTo: SetTypingToUseCase,
    val observeProfile: ObserveProfileUseCase,
    val userPresenceUseCase: SetUserPresenceUseCase
)