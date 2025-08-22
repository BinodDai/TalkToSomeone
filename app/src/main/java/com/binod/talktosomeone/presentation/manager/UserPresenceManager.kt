package com.binod.talktosomeone.presentation.manager

import com.binod.talktosomeone.domain.aggregator.ProfileUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPresenceManager @Inject constructor(
    private val profileUseCase: ProfileUseCases,
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun setUserOnline() {
        scope.launch {
            try {
                profileUseCase.userPresenceUseCase.setOnline()
            } catch (e: Exception) {
                // Log error
            }
        }
    }

    fun setUserOffline() {
        scope.launch {
            try {
                profileUseCase.userPresenceUseCase.setOffline()
            } catch (e: Exception) {
                // Log error
            }
        }

    }

    fun cleanup() {
        scope.cancel()
    }
}