package com.binod.talktosomeone.di

import com.binod.talktosomeone.data.remote.api.FirestoreService
import com.binod.talktosomeone.data.repository.ChatRepositoryImpl
import com.binod.talktosomeone.data.repository.HomeRepositoryImpl
import com.binod.talktosomeone.data.repository.ProfileRepositoryImpl
import com.binod.talktosomeone.domain.repository.ChatRepository
import com.binod.talktosomeone.domain.repository.HomeRepository
import com.binod.talktosomeone.domain.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirestoreService(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ) = FirestoreService(firestore, auth)

    @Provides
    @Singleton
    fun provideProfileRepository(service: FirestoreService): ProfileRepository =
        ProfileRepositoryImpl(service)

    @Provides
    @Singleton
    fun provideChatRepository(
        service: FirestoreService
    ): ChatRepository = ChatRepositoryImpl(service)

    @Provides
    @Singleton
    fun provideHomeRepository(
        service: FirestoreService
    ): HomeRepository = HomeRepositoryImpl(service)
}