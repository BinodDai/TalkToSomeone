package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.repository.HomeRepository
import javax.inject.Inject

class FindQuickMatchUseCase @Inject constructor(
    private val repo: HomeRepository
) {
    suspend operator fun invoke(currentUserAge: Int? = null, gender: String? = null): Profile? =
        repo.findQuickMatch(currentUserAge, gender)
}
