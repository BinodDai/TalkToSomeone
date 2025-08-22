package com.binod.talktosomeone.domain.aggregator

import com.binod.talktosomeone.domain.usecase.FindQuickMatchUseCase
import com.binod.talktosomeone.domain.usecase.GetOnlinePeopleCountUseCase
import com.binod.talktosomeone.domain.usecase.GetTodayChatsUseCase
import javax.inject.Inject

class HomeUseCases @Inject constructor(
    val findQuickMatch: FindQuickMatchUseCase,
    val getOnlinePeopleCount: GetOnlinePeopleCountUseCase,
    val getTodayChats: GetTodayChatsUseCase
)