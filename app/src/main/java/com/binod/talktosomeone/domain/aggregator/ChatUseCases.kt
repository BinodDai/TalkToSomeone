package com.binod.talktosomeone.domain.aggregator

import com.binod.talktosomeone.domain.usecase.AddReactionUseCase
import com.binod.talktosomeone.domain.usecase.GetMessageByIdUseCase
import com.binod.talktosomeone.domain.usecase.MarkDeliveredUseCase
import com.binod.talktosomeone.domain.usecase.MarkSeenUseCase
import com.binod.talktosomeone.domain.usecase.ObserveMessagesUseCase
import com.binod.talktosomeone.domain.usecase.SendMessageUseCase
import javax.inject.Inject

class ChatUseCases @Inject constructor(
    val sendMessage: SendMessageUseCase,
    val observeMessages: ObserveMessagesUseCase,
    val markDelivered: MarkDeliveredUseCase,
    val markSeen: MarkSeenUseCase,
    val addReaction: AddReactionUseCase,
    val getMessageById: GetMessageByIdUseCase,
)