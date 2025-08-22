package com.binod.talktosomeone.data.remote.api

import android.util.Log
import com.binod.talktosomeone.domain.model.ChatMessage
import com.binod.talktosomeone.domain.model.ChatSummary
import com.binod.talktosomeone.domain.model.MessageStatus
import com.binod.talktosomeone.domain.model.Profile
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
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

    private val profilesCollection = firestore.collection("profiles")
    private val chats = firestore.collection("chats")

    fun currentUserId(): String? = auth.currentUser?.uid

    // ---------------------- Profile ----------------------
    suspend fun createOrUpdateProfile(profile: Profile) {
        profilesCollection.document(profile.userId).set(profile).await()
    }

    suspend fun getProfile(userId: String): Profile? =
        profilesCollection.document(userId).get().await().toObject(Profile::class.java)

    fun observeProfile(userId: String): Flow<Profile?> = callbackFlow {
        val reg = profilesCollection.document(userId).addSnapshotListener { snap, _ ->
            trySend(snap?.toObject<Profile>())
        }
        awaitClose { reg.remove() }
    }

    fun signInAnonymously(): Task<AuthResult> = auth.signInAnonymously()

    suspend fun setOnline(isOnline: Boolean) {
        val userId = currentUserId() ?: return
        val data = if (isOnline)
            mapOf("online" to true, "lastSeen" to null)
        else
            mapOf("online" to false, "lastSeen" to System.currentTimeMillis())

        profilesCollection.document(userId)
            .update(data).await()
    }

    suspend fun setTypingTo(userId: String, typingTo: String?) {
        profilesCollection.document(userId).update("typingTo", typingTo).await()
    }

    // ---------------------- Chat ----------------------
    private fun messagesRef(chatId: String) =
        chats.document(chatId).collection("messages")

    fun chatIdFor(userA: String, userB: String): String =
        if (userA < userB) "${userA}_$userB" else "${userB}_$userA"

    suspend fun sendMessage(message: ChatMessage) {
        require(message.senderId.isNotBlank() && message.receiverId.isNotBlank())
        val chatId = chatIdFor(message.senderId, message.receiverId)
        val payload = message.copy(chatId = chatId)

        messagesRef(chatId).add(payload).await()

        // Update last message in chat summary
        updateChatSummary(
            ChatSummary(
                chatId = chatId,
                userA = chatId.substringBefore('_'),
                userB = chatId.substringAfter('_'),
                lastMessage = payload.text ?: (payload.imageUrl?.let { "[image]" } ?: ""),
                lastTimestamp = payload.timestamp,
                lastSenderId = payload.senderId
            )
        )
    }

    fun observeMessages(chatId: String): Flow<List<ChatMessage>> = callbackFlow {
        val reg = messagesRef(chatId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents?.mapNotNull { it.toObject<ChatMessage>() } ?: emptyList()
                trySend(list)
            }
        awaitClose { reg.remove() }
    }

    suspend fun markDelivered(chatId: String, myUserId: String) {
        val q = messagesRef(chatId)
            .whereEqualTo("receiverId", myUserId)
            .whereEqualTo("status", MessageStatus.SENT.name)
            .get().await()
        q.documents.forEach { it.reference.update("status", MessageStatus.DELIVERED.name).await() }
    }

    suspend fun markSeen(chatId: String, myUserId: String) {
        val q = messagesRef(chatId)
            .whereEqualTo("receiverId", myUserId)
            .whereIn("status", listOf(MessageStatus.SENT.name, MessageStatus.DELIVERED.name))
            .get().await()
        q.documents.forEach { it.reference.update("status", MessageStatus.SEEN.name).await() }
    }

    suspend fun addReaction(chatId: String, messageId: String, userId: String, emoji: String) {
        val messageRef = messagesRef(chatId).document(messageId)
        firestore.runTransaction { tx ->
            val snapshot = tx.get(messageRef)
            val currentReactions = snapshot.get("reactions") as? Map<String, String> ?: emptyMap()
            val updated = currentReactions.toMutableMap()
            updated[userId] = emoji
            tx.update(messageRef, "reactions", updated)
        }.await()
    }

    suspend fun getMessageById(chatId: String, messageId: String): ChatMessage? =
        messagesRef(chatId).document(messageId).get().await().toObject(ChatMessage::class.java)

    private suspend fun updateChatSummary(summary: ChatSummary) {
        chats.document(summary.chatId).set(summary).await()
    }

    fun observeMyChats(myUserId: String): Flow<List<ChatSummary>> = callbackFlow {
        val reg = chats
            .whereArrayContainsAny("participants", listOf(myUserId))
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents?.mapNotNull { it.toObject<ChatSummary>() } ?: emptyList()
                trySend(list.sortedByDescending { it.lastTimestamp })
            }
        awaitClose { reg.remove() }
    }

    suspend fun findQuickMatch(
        currentUserAge: Int? = null,
        gender: String? = null
    ): Profile? {
        var query: Query = profilesCollection
            .whereEqualTo("online", true)
            .whereNotEqualTo("userId", currentUserId())

        currentUserAge?.let { age ->
            val minAge = maxOf(age - 3, 18)
            val maxAge = age + 3
            query = query
                .whereGreaterThanOrEqualTo("age", minAge)
                .whereLessThanOrEqualTo("age", maxAge)
        }

        gender?.let { query = query.whereEqualTo("gender", it) }

        query = query.limit(1)

        val snapshot = query.get().await()
        Log.d("QuickMatch", "Query returned ${snapshot.documents.size} documents")

        return snapshot.documents.firstOrNull()?.toObject(Profile::class.java)
    }

}

