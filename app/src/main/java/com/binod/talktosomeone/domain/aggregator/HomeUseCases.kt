package com.binod.talktosomeone.domain.aggregator

import com.binod.talktosomeone.domain.usecase.FindQuickMatchUseCase
import javax.inject.Inject

class HomeUseCases @Inject constructor(
    val findQuickMatch: FindQuickMatchUseCase
)