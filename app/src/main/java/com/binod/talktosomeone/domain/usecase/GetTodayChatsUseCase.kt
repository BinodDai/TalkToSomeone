package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.model.ChatSummary
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.repository.HomeRepository
import javax.inject.Inject

class GetTodayChatsUseCase @Inject constructor(
    private val repo: HomeRepository
) {
    suspend operator fun invoke(): List<ChatSummary> =
        repo.getTodayChats()
}
