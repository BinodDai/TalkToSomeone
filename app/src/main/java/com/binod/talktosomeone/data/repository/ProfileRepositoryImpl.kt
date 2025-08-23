package com.binod.talktosomeone.data.repository

import com.binod.talktosomeone.data.remote.api.FirestoreService
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ProfileRepositoryImpl @Inject constructor(
    private val service: FirestoreService,
) : ProfileRepository {
    private val _profileFlow = MutableStateFlow<Profile?>(null)
    override val profileFlow: Flow<Profile?> = _profileFlow

    override suspend fun setOnline(isOnline: Boolean) {
        service.setOnlineFlag(isOnline)
    }

    override suspend fun createProfile(profile: Profile) {
        service.saveProfile(profile)
    }

    override suspend fun refreshProfile(userId: String): Profile? {
        val profile = service.fetchProfile(userId)
        _profileFlow.value = profile
        return profile
    }

    override suspend fun getProfile(userId: String): Profile? {
        return  refreshProfile(userId)
    }

    override suspend fun signInAnonymously(): String {
        return suspendCoroutine { cont ->
            service.signInAnonymously()
                .addOnSuccessListener { cont.resume(it.user!!.uid) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }

    override suspend fun setTypingTo(userId: String, typingTo: String?) {
        service.updateTypingTo(userId, typingTo)
    }

    override fun observeProfile(userId: String): Flow<Profile?> =
        service.observeProfile(userId)

}
