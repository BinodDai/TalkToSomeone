package com.binod.talktosomeone.data.repository

import com.binod.talktosomeone.data.remote.api.FirestoreService
import com.binod.talktosomeone.domain.model.ChatSummary
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.repository.HomeRepository
import com.binod.talktosomeone.utils.getStartOfDayTimestamp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val service: FirestoreService
) : HomeRepository {
    override suspend fun findQuickMatch(currentUserAge: Int?, gender: String?): Profile? {
        val candidates = service.findOnlineProfilesExcludingCurrent()
        return candidates
            .filter { profile ->
                (currentUserAge == null || (profile.age in (currentUserAge - 3)..(currentUserAge + 3))) &&
                        (gender == null || profile.gender == gender)
            }
            .randomOrNull()
    }

    override suspend fun getOnlinePeopleCount(): Int =
        service.countOnlineProfiles()

    override suspend fun getTodayChats(): List<ChatSummary> {
        val userId = service.currentUserId() ?: return emptyList()
        val startOfDay = getStartOfDayTimestamp()
        return service.getChatsSince(userId, startOfDay)
    }

}