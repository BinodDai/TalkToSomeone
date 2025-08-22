package com.binod.talktosomeone.data.repository

import com.binod.talktosomeone.data.remote.api.FirestoreService
import com.binod.talktosomeone.domain.model.ChatSummary
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val service: FirestoreService
) : HomeRepository {
    override suspend fun findQuickMatch(currentUserAge: Int?, gender: String?): Profile? =
        service.findQuickMatch(currentUserAge, gender)

    override suspend fun getOnlinePeopleCount(): Int = service.getOnlinePeopleCount()

    override suspend fun getTodayChats(): List<ChatSummary> {
        return service.getTodayChats()
    }

}