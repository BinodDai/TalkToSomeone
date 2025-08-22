package com.binod.talktosomeone.domain.repository

import com.binod.talktosomeone.domain.model.Profile

interface HomeRepository {
    suspend fun findQuickMatch(currentUserAge: Int? = null, gender: String? = null): Profile?
}