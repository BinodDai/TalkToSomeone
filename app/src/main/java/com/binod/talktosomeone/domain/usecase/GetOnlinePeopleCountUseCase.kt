package com.binod.talktosomeone.domain.usecase

import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.repository.HomeRepository
import javax.inject.Inject

class GetOnlinePeopleCountUseCase @Inject constructor(
    private val repo: HomeRepository
) {
    suspend operator fun invoke(): Int = repo.getOnlinePeopleCount()
}
