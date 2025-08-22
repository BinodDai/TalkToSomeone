package com.binod.talktosomeone.data.remote.api

import com.binod.talktosomeone.domain.model.ChatMessage
import com.binod.talktosomeone.domain.model.ChatSummary
import com.binod.talktosomeone.domain.model.MessageStatus
import com.binod.talktosomeone.domain.model.Profile
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val profiles = firestore.collection("profiles")
    private val chats = firestore.collection("chats")

    fun currentUserId(): String? = auth.currentUser?.uid

    // ---------- Profile ----------
    suspend fun saveProfile(profile: Profile) {
        profiles.document(profile.userId).set(profile).await()
    }

    suspend fun fetchProfile(userId: String): Profile? =
        profiles.document(userId).get().await().toObject(Profile::class.java)

    fun observeProfile(userId: String): Flow<Profile?> = callbackFlow {
        val reg = profiles.document(userId).addSnapshotListener { snap, _ ->
            trySend(snap?.toObject<Profile>())
        }
        awaitClose { reg.remove() }
    }

    fun signInAnonymously(): Task<AuthResult> = auth.signInAnonymously()

    suspend fun setOnlineFlag(isOnline: Boolean) {
        val userId = currentUserId() ?: return
        val data = if (isOnline)
            mapOf("online" to true, "lastSeen" to null)
        else
            mapOf("online" to false, "lastSeen" to System.currentTimeMillis())
        profiles.document(userId).update(data).await()
    }

    suspend fun updateTypingTo(userId: String, typingTo: String?) {
        profiles.document(userId).update("typingTo", typingTo).await()
    }

    suspend fun findOnlineProfilesExcludingCurrent(): List<Profile> {
        val snapshot = profiles
            .whereEqualTo("online", true)
            .whereNotEqualTo("userId", currentUserId())
            .get().await()
        return snapshot.documents.mapNotNull { it.toObject(Profile::class.java) }
    }

    suspend fun countOnlineProfiles(): Int =
        profiles.whereEqualTo("online", true).get().await().size()

    // ---------- Chat ----------
    fun chatIdFor(userA: String, userB: String): String =
        if (userA < userB) "${userA}_$userB" else "${userB}_$userA"

    private fun messagesRef(chatId: String) =
        chats.document(chatId).collection("messages")

    suspend fun addMessage(chatId: String, message: ChatMessage) {
        messagesRef(chatId).add(message).await()
    }

    fun observeMessages(chatId: String): Flow<List<ChatMessage>> = callbackFlow {
        val reg = messagesRef(chatId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snap, _ ->
                trySend(snap?.documents?.mapNotNull { it.toObject<ChatMessage>() } ?: emptyList())
            }
        awaitClose { reg.remove() }
    }

    suspend fun findMessagesByStatus(chatId: String, userId: String, status: MessageStatus) =
        messagesRef(chatId)
            .whereEqualTo("receiverId", userId)
            .whereEqualTo("status", status.name)
            .get().await().documents

    suspend fun findMessagesByStatuses(
        chatId: String,
        userId: String,
        statuses: List<MessageStatus>
    ) = messagesRef(chatId)
        .whereEqualTo("receiverId", userId)
        .whereIn("status", statuses.map { it.name })
        .get().await().documents

    suspend fun updateMessageStatus(ref: DocumentReference, status: MessageStatus) {
        ref.update("status", status.name).await()
    }

    suspend fun updateReaction(chatId: String, messageId: String, userId: String, emoji: String) {
        val messageRef = messagesRef(chatId).document(messageId)
        firestore.runTransaction { tx ->
            val snapshot = tx.get(messageRef)
            val reactions = snapshot.get("reactions") as? Map<String, String> ?: emptyMap()
            tx.update(messageRef, "reactions", reactions + (userId to emoji))
        }.await()
    }

    suspend fun getMessageById(chatId: String, messageId: String): ChatMessage? =
        messagesRef(chatId).document(messageId).get().await().toObject(ChatMessage::class.java)

    suspend fun updateChatSummary(summary: ChatSummary) {
        chats.document(summary.chatId).set(summary).await()
    }

    fun observeMyChats(myUserId: String): Flow<List<ChatSummary>> = callbackFlow {
        val reg = chats
            .whereArrayContains("participants", myUserId)
            .addSnapshotListener { snap, _ ->
                trySend(
                    snap?.documents
                        ?.mapNotNull { it.toObject<ChatSummary>() }
                        ?.sortedByDescending { it.lastTimestamp }
                        ?: emptyList()
                )
            }
        awaitClose { reg.remove() }
    }

    suspend fun getChatsSince(userId: String, startOfDay: Long): List<ChatSummary> {
        val snapshot = chats
            .whereArrayContains("participants", userId)
            .whereGreaterThanOrEqualTo("lastTimestamp", startOfDay)
            .get().await()
        return snapshot.documents.mapNotNull { it.toObject(ChatSummary::class.java) }
    }

}

